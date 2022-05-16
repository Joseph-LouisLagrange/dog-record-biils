package com.darwin.dog.service.impl;

import com.darwin.dog.constant.BillDeleteType;
import com.darwin.dog.constant.BillType;
import com.darwin.dog.constant.Coins;
import com.darwin.dog.constant.ObjectsCode;
import com.darwin.dog.dto.in.*;
import com.darwin.dog.dto.mapper.BillMapper;
import com.darwin.dog.dto.out.*;
import com.darwin.dog.exception.BaseExceptionType;
import com.darwin.dog.exception.CommonException;
import com.darwin.dog.po.*;
import com.darwin.dog.repository.BillRepository;
import com.darwin.dog.service.inf.AccountService;
import com.darwin.dog.service.inf.BillService;
import com.darwin.dog.service.inf.CurrencyExchangeRateService;
import com.darwin.dog.service.inf.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private LedgerService ledgerService;

    @Autowired
    private CurrencyExchangeRateService currencyExchangeRateService;

    @Autowired
    private AccountService accountService;


    public Set<Bill> getMyBillsByDateRanges(List<Range> ranges) {
        return ranges
                .parallelStream()
                .map(range -> getMyBillsByDateRange(
                        range.startDateTime, range.endDateTime)
                )
                .flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public Set<Bill> getMyBillsByDateRanges(List<Range> ranges, BillType billType) {
        return ranges
                .parallelStream()
                .map(range -> getMyBillsByDateRange(
                        range.startDateTime, range.endDateTime, billType)
                )
                .flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public Long getRangesDayCount(List<Range> ranges) {
        long ans = 0;
        for (Range range : ranges) {
            ans += range.startDateTime.until(range.endDateTime, ChronoUnit.DAYS) + 1;
        }
        return ans;
    }

    public Map<String, List<Bill>> group(Set<Bill> bills, TemporalUnit temporalUnit) {
        if (temporalUnit.equals(ChronoUnit.MONTHS)) {
            return bills.parallelStream().collect(Collectors
                    .groupingBy(bill -> bill.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-01 00:00:00"))));
        }
        return bills.parallelStream().collect(Collectors
                .groupingBy(bill -> bill.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"))));
    }

    public Optional<Bill> getHighestBill(Set<Bill> bills) {
        return bills.parallelStream().max(Comparator.comparing(bill -> bill.getAmount().abs()));
    }

    public MoneyTrendOutDTO countMoneyTrend0(QueryRangesWithBillTypeInDTO queryRangesInDTO,
                                             TemporalUnit temporalUnit, Long dd) {

        Set<Bill> set = getMyBillsByDateRanges(queryRangesInDTO.ranges, queryRangesInDTO.billType);
        if (set.isEmpty()) return new MoneyTrendOutDTO();
        List<MoneyTrendOutDTO.AmountAtDate> amountAtDates = new ArrayList<>();
        AtomicReference<Bill> highestBillAtomic = new AtomicReference<>();
        AtomicReference<BigDecimal> sumAtomic = new AtomicReference<>(BigDecimal.ZERO);
        CompletableFuture.supplyAsync(
                        () -> group(set, temporalUnit))
                .whenCompleteAsync((map, throwable) -> {
                    for (Map.Entry<String, List<Bill>> entry : map.entrySet()) {
                        String day = entry.getKey();
                        List<Bill> bills = entry.getValue();
                        BigDecimal sumDay = sum(bills, Coins.CNY);
                        BigDecimal old = sumAtomic.get();
                        while (!sumAtomic.compareAndSet(old, old.add(sumDay))) {
                            old = sumAtomic.get();
                        }
                        amountAtDates.add(new MoneyTrendOutDTO.AmountAtDate(sumDay.doubleValue(), day));
                    }
                }).join();
        MoneyTrendOutDTO.AmountAtDate highestD = amountAtDates
                .stream()
                .max(Comparator.comparing(m -> Math.abs(m.getAmount()))).get();
        BigDecimal average = sumAtomic.get().divide(BigDecimal.valueOf(dd), RoundingMode.CEILING);
        BigDecimal highest = BigDecimal.valueOf(highestD.getAmount());
        LocalDateTime highestDateTime = LocalDateTime.parse(highestD.getDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return MoneyTrendOutDTO
                .builder()
                .amountAtDates(amountAtDates)
                .average(average.doubleValue())
                .billCount((long) set.size())
                .highestDateTime(highestDateTime)
                .highest(highest.doubleValue())
                .build();
    }

    @Override
    public boolean removeCompletely(Set<Long> IDs) {
        billRepository.deleteAllByIDIsIn(IDs);
        return true;
    }

    @Override
    public long queryDeletedBillTypeCount() {
        return billRepository.count((Specification<Bill>) (root, query, criteriaBuilder) -> query.where(
                criteriaBuilder.equal(root.get("user").as(User.class), getMe()),
                criteriaBuilder.equal(root.get("deleteType").as(Integer.class), BillDeleteType.BILL_DELETE)
        ).getRestriction());
    }

    @Override
    public boolean recover(Long ID) {
        Bill bill = billRepository.getOne(ID);
        int billDeleteType = bill.getDeleteType();
        bill.setDeleteType((~BillDeleteType.BILL_DELETE) & billDeleteType);
        Account account = bill.getAccount();
        if (account != null) {
            // 要把资金还原
            accountService.addBalance(
                    account.getID(),
                    bill.getCoin(),
                    bill.getAmount());
        }
        billRepository.save(bill);
        return true;
    }


    @Override
    public boolean recover(Set<Long> IDs) {
        List<Bill> bills = billRepository.findAllById(IDs);
        bills.forEach(bill -> {
            int billDeleteType = bill.getDeleteType();
            bill.setDeleteType((~BillDeleteType.BILL_DELETE) & billDeleteType);
            Account account = bill.getAccount();
            if (account != null) {
                accountService.addBalance(
                        account.getID(),
                        bill.getCoin(),
                        bill.getAmount());
            }
        });
        billRepository.saveAll(bills);
        return true;
    }

    @Override
    public boolean reduce(List<Bill> bills, int billDeleteType) {
        bills.forEach(bill -> {
            bill.setDeleteType((~billDeleteType) & bill.getDeleteType());
            Account account = bill.getAccount();
            if (account != null) {
                // 要把资金还原
                accountService.addBalance(
                        account.getID(),
                        bill.getCoin(),
                        bill.getAmount());
            }
        });
        billRepository.saveAll(bills);
        return true;
    }

    @Override
    public List<BillsBlockDTO> readDeletedBillTypeBills() {
        return genBillsBlockDTO(Coins.CNY, billRepository.findByUserAndDeleteType(getMe(), BillDeleteType.BILL_DELETE));
    }

    @Override
    public List<BillsBlockDTO> readDeletedBills() {
        return genBillsBlockDTO(
                Coins.CNY,
                billRepository.findAll((Specification<Bill>) (root, query, cb) -> query.where(
                        cb.equal(root.get("user"), getMe()),
                        cb.notEqual(root.get("deleteType"), BillDeleteType.NO_DELETE)
                ).getRestriction())
        );
    }

    @Override
    public long queryDeletedCount() {
        return billRepository.countDeleted(getMe().getID());
    }

    @Override
    public long queryBillsCount() {
        return billRepository.countBills(getMe().getID());
    }

    @Override
    public AssetsRangeDetailDTO readBillsInAccount(QueryRangeAtAccountInDTO queryRangeAtAccountInDTO) {
        Account account = accountService.getOne(queryRangeAtAccountInDTO.accountID);
        Coin baseCoin = account.getCoin();
        List<Bill> bills = queryRangeAtAccountInDTO.getRanges()
                .parallelStream()
                .map(range -> billRepository.findAllInDateTimeAndAccount(
                        account, range.startDateTime, range.endDateTime))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<BillsBlockDTO> billsBlockDTOS = genBillsBlockDTO(baseCoin, bills);
        return AssetsRangeDetailDTO.builder()
                .coin(baseCoin)
                .billsBlockDTOS(billsBlockDTOS)
                .inflow(getIncome(baseCoin, bills).doubleValue())
                .outflow(getExpense(baseCoin, bills).doubleValue())
                .build();

    }

    @Override
    public List<Bill> countBillRanking(QueryRangesWithBillTypeInDTO queryRangesWithBillTypeInDTO) {
        Set<Bill> bills = getMyBillsByDateRanges(queryRangesWithBillTypeInDTO.ranges, queryRangesWithBillTypeInDTO.billType);
        return bills.parallelStream()
                .sorted(
                        Comparator
                                .comparing(bill -> currencyExchangeRateService
                                        .exchange(((Bill) bill).getCoin().getShortName(),
                                                ((Bill) bill).getAmount().abs(), Coins.CNY))
                                .reversed()
                )
                .limit(5)
                .collect(Collectors.toList());


    }

    @Override
    public List<CategoryRankingItemDTO> countCategoryRanking(QueryRangesWithBillTypeInDTO queryRangesWithBillTypeInDTO) {
        Set<Bill> bills = getMyBillsByDateRanges(queryRangesWithBillTypeInDTO.ranges, queryRangesWithBillTypeInDTO.billType);
        Map<Signory, List<Bill>> map = bills.stream().collect(Collectors.groupingBy(Bill::getSignory));
        return map.entrySet().stream()
                .map(signoryListEntry -> {
                    CategoryRankingItemDTO categoryRankingItemDTO = new CategoryRankingItemDTO();
                    categoryRankingItemDTO.setSignory(signoryListEntry.getKey());
                    categoryRankingItemDTO.setBillsCount((long) signoryListEntry.getValue().size());
                    categoryRankingItemDTO.setMoney(sum(signoryListEntry.getValue(), Coins.CNY).doubleValue());
                    return categoryRankingItemDTO;
                })
                .sorted(Comparator.comparingDouble(categoryRankingItemDTO -> Math.abs(((CategoryRankingItemDTO) categoryRankingItemDTO).getMoney())).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public List<MoneySignoryPartOutDTO> countMoneySignoryPart(QueryRangesWithBillTypeInDTO queryRangesWithBillTypeInDTO) {
        Set<Bill> bills = getMyBillsByDateRanges(queryRangesWithBillTypeInDTO.ranges, queryRangesWithBillTypeInDTO.billType);
        Map<Signory, Double> cntMap = new HashMap<>();
        bills.forEach(bill -> cntMap.put(bill.getSignory(), cntMap.getOrDefault(bill.getSignory(), 0d) + 1d));
        int sum = bills.size();
        return cntMap.entrySet()
                .stream()
                .map(a -> new MoneySignoryPartOutDTO(a.getKey(), a.getValue() / sum))
                .collect(Collectors.toList());
    }

    @Override
    public MoneyTrendOutDTO countMoneyTrend(QueryRangesWithBillTypeInDTO queryRangesInDTO) {
        TemporalUnit temporalUnit = ChronoUnit.DAYS;
        long dd = getRangesDayCount(queryRangesInDTO.ranges);
        if (queryRangesInDTO.mode.equals("year")) {
            temporalUnit = ChronoUnit.MONTHS;
            dd = 12;
        }
        return countMoneyTrend0(queryRangesInDTO, temporalUnit, dd);
    }

    @Override
    public AmountDTO countMyAmount(QueryRangesInDTO queryRangesInDTO) {
        List<Bill> bills = queryRangesInDTO.ranges.parallelStream()
                .map(range -> getMyBillsByDateRange(range.startDateTime, range.endDateTime))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        BigDecimal expense = sum(bills.stream().filter(bill -> bill.getType().equals(BillType.EXPENSE)).collect(Collectors.toList()), Coins.CNY);
        BigDecimal income = sum(bills.stream().filter(bill -> bill.getType().equals(BillType.INCOME)).collect(Collectors.toList()), Coins.CNY);
        BigDecimal surplus = income.add(expense);
        return AmountDTO.builder()
                .expense(expense.doubleValue())
                .income(income.doubleValue())
                .surplus(surplus.doubleValue())
                .build();
    }

    public List<Bill> getMyBillsByDateRange(LocalDateTime start, LocalDateTime end, BillType billType) {
        return billRepository.findAllInDateTimeAndBillType(getMe(), start, end, billType, Sort.by("dateTime").descending());
    }

    @Override
    public List<Bill> getMyBillsByDateRange(LocalDateTime start, LocalDateTime end) {
        return billRepository.findAllInDateTime(getMe(), start, end, Sort.by("dateTime").descending());
    }

    @Override
    public boolean createBill(CreateBillInDTO createBillInDTO) {
        Bill bill = billMapper.from(createBillInDTO, getMe());
        Account account = bill.getAccount();
        if (account != null) {
            accountService.addBalance(
                    account.getID(),
                    bill.getCoin(),
                    bill.getAmount());
        }
        billRepository.save(bill);
        return true;
    }

    @Override
    public boolean delete(Long ID) {
        billRepository.updateBillDeleteType(ID, BillDeleteType.BILL_DELETE);
        // 资金还原
        Bill bill = readByID(ID);
        Account account = bill.getAccount();
        if (account != null) {
            accountService.subtractBalance(account.getID(), bill.getCoin(), bill.getAmount());
        }
        return true;
    }

    @Override
    public Bill readByID(Long ID) {
        return billRepository.findById(ID).orElseThrow(() -> CommonException.of(
                BaseExceptionType.MISS, ObjectsCode.BILL, String.format("billID=%d 未命中 Bill", ID)
        ));
    }

    @Override
    public boolean deleteInLedger(Long ledgerID) {
        // 资金还原
        billRepository.findAllByLedgerID(ledgerID)
                .parallelStream()
                .forEach(bill -> {
                    Account account = bill.getAccount();
                    if (account != null) {
                        accountService.subtractBalance(
                                account.getID(),
                                bill.getCoin(),
                                bill.getAmount());
                    }
                });
        billRepository.deleteBillsInLedger(ledgerID, BillDeleteType.LEDGER_DELETE);
        return true;
    }

    @Override
    public boolean deleteInAccount(Long accountID) {
        billRepository.deleteBillsInAccount(accountID, BillDeleteType.ACCOUNT_DELETE);
        return true;
    }

    @Override
    public Bill update(UpdateBillDTO updateBillDTO) {
        Bill bill = readByID(updateBillDTO.getBillID());
        Account oldAccount = bill.getAccount();
        // 1.把钱还给账户，无论支出、收入与否
        if (oldAccount != null) {
            accountService.subtractBalance(
                    oldAccount.getID(),
                    bill.getCoin(),
                    bill.getAmount());
        }
        billMapper.updateBillDTOToBill(updateBillDTO, bill);
        Account curAccount = bill.getAccount();
        // 2.把金钱计算到这个账户上
        if (curAccount != null) {
            accountService.addBalance(
                    curAccount.getID(),
                    bill.getCoin(),
                    bill.getAmount());
        }
        return billRepository.saveAndFlush(bill);
    }

    @Override
    public LedgerRangeDetailDTO readLedgerForDateRanges(QueryRangeAtLedgerInDTO queryRangeAtLedgerInDTO) {
        Ledger ledger = ledgerService.getLedgerByID(queryRangeAtLedgerInDTO.ledgerID);
        Coin baseCoin = ledger.getCoin(); // 本位币
        List<Bill> bills = queryRangeAtLedgerInDTO.getRanges()
                .parallelStream()
                .map(range -> billRepository.findAllInDateTimeAndLedger(
                        ledger, range.startDateTime, range.endDateTime, Sort.unsorted()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        BigDecimal income = getIncome(baseCoin, bills);
        BigDecimal expense = getExpense(baseCoin, bills);
        return LedgerRangeDetailDTO.builder()
                .coin(baseCoin)
                .income(income.doubleValue())
                .expense(income.doubleValue())
                .surplus(income.add(expense).doubleValue())
                .blocks(genBillsBlockDTO(baseCoin, bills))
                .build();
    }

    private List<BillsBlockDTO> genBillsBlockDTO(Coin baseCoin, List<Bill> bills) {
        return genBillsBlockDTO(baseCoin.getShortName(), bills);
    }

    private List<BillsBlockDTO> genBillsBlockDTO(String baseCoin, List<Bill> bills) {
        // 分割 bills 为块组（以 Day 为 key）
        Map<LocalDateTime, List<Bill>> temp = bills
                .parallelStream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getDateTime().truncatedTo(ChronoUnit.DAYS)
                ));
        List<BillsBlockDTO> blocks = new ArrayList<>();
        Set<Map.Entry<LocalDateTime, List<Bill>>> entries = temp.entrySet();
        for (Map.Entry<LocalDateTime, List<Bill>> entry : entries) {
            // 生成 block
            entry.getValue().sort(Comparator.comparing(Bill::getDateTime).reversed());
            blocks.add(billMapper.toBillsBlockDTO(sum(entry.getValue(), baseCoin), entry.getValue(), entry.getKey(), null));
        }
        // 对 block 进行排序
        blocks.sort(Comparator.comparing(BillsBlockDTO::getDateTime).reversed());
        return blocks;
    }

    private BigDecimal getIncome(Coin baseCoin, List<Bill> bills) {
        List<Bill> incomeBills = bills
                .parallelStream()
                .filter(bill -> bill.getType().equals(BillType.INCOME))
                .collect(Collectors.toList());
        return sum(incomeBills, baseCoin);
    }

    private BigDecimal getExpense(Coin baseCoin, List<Bill> bills) {
        List<Bill> expenseBills = bills
                .parallelStream()
                .filter(bill -> bill.getType().equals(BillType.EXPENSE))
                .collect(Collectors.toList());
        return sum(expenseBills, baseCoin);
    }


    @Override
    public LedgerRangeDetailDTO getMyBillsForDateRangeAndLedger(Long ledgerID, LocalDateTime start, LocalDateTime end) {
        Ledger ledger = ledgerService.getLedgerByID(ledgerID);
        List<Bill> bills = billRepository.findAllInDateTimeAndLedger(ledger, start, end, Sort.by("dateTime").descending());
        Coin baseCoin = ledger.getCoin(); // 本位币
        BigDecimal income = getIncome(baseCoin, bills);
        BigDecimal expense = getExpense(baseCoin, bills);
        return LedgerRangeDetailDTO.builder()
                .coin(baseCoin)
                .income(income.doubleValue())
                .expense(income.doubleValue())
                .surplus(income.add(expense).doubleValue())
                .blocks(genBillsBlockDTO(baseCoin, bills))
                .build();
    }

    @Override
    public List<Bill> searchForKeyword(@NotBlank String keyword) {
        return billRepository.findBillsByUserEqualsAndRemarkIsLike(getMe(), "%" + keyword + "%");
    }

    @Override
    public BigDecimal sum(List<Bill> bills, Coin baseCoin) {
        return sum(bills, baseCoin.getShortName());
    }

    @Override
    public BigDecimal sum(List<Bill> bills, String baseCoin) {
        return bills.parallelStream()
                .map(bill -> currencyExchangeRateService
                        .exchange(
                                bill.getCoin().getShortName(), bill.getAmount(), baseCoin
                        ))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

}
