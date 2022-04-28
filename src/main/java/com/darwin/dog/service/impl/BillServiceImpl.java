package com.darwin.dog.service.impl;

import com.darwin.dog.dto.in.CreateBillInDTO;
import com.darwin.dog.dto.in.QueryRangeInDTO;
import com.darwin.dog.dto.mapper.BillMapper;
import com.darwin.dog.dto.out.BillsBlockDTO;
import com.darwin.dog.dto.out.LedgerRangeDetailDTO;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

    @Override
    public List<Bill> getMyBillsByDateRange(LocalDateTime start, LocalDateTime end) {
        return billRepository.findAllInDateTime(getMe(), start, end, Sort.by("dateTime").descending());
    }

    @Override
    public boolean createBill(CreateBillInDTO createBillInDTO) {
        Bill bill = billMapper.from(createBillInDTO, getMe());
        Account account = bill.getAccount();
        if (account != null) {
            account.setBalance(account.getBalance().add(bill.getAmount()));
        }
        billRepository.save(bill);
        return true;
    }

    @Override
    public boolean delete(Long ID) {
        return false;
    }

    @Override
    public boolean deleteInLedger(Long ledgerID) {
        return false;
    }

    @Override
    public LedgerRangeDetailDTO readLedgerForDateRanges(QueryRangeInDTO queryRangeInDTO) {
        Ledger ledger = ledgerService.getLedgerByID(queryRangeInDTO.ledgerID);
        List<Bill> bills = queryRangeInDTO.getRanges()
                .parallelStream()
                .map(range -> billRepository.findAllInDateTimeAndLedger(
                        ledger, range.startDateTime, range.endDateTime, Sort.unsorted()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return loveYou(ledger.getCoin(),bills);
    }

    private LedgerRangeDetailDTO loveYou(Coin baseCoin,List<Bill> bills){
        LedgerRangeDetailDTO ledgerRangeDetailDTO = new LedgerRangeDetailDTO();
        ledgerRangeDetailDTO.setCoin(baseCoin);
        List<Bill> incomeBills = bills
                .parallelStream()
                .filter(bill -> bill.getAmount().compareTo(BigDecimal.ZERO) >= 0)
                .collect(Collectors.toList());
        BigDecimal income = sum(incomeBills, baseCoin);
        ledgerRangeDetailDTO.setIncome(income.doubleValue());
        List<Bill> expenseBills = bills
                .parallelStream()
                .filter(bill -> bill.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .collect(Collectors.toList());
        BigDecimal expense = sum(expenseBills, baseCoin);
        ledgerRangeDetailDTO.setExpense(Math.abs(expense.doubleValue()));
        ledgerRangeDetailDTO.setSurplus(income.add(expense).doubleValue());
        // 分割 bills 为块组（以 Day 为 key）
        Map<LocalDateTime, List<Bill>> temp = bills.parallelStream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getDateTime().truncatedTo(ChronoUnit.DAYS)
                ));
        List<BillsBlockDTO> blocks = new ArrayList<>();
        for (Map.Entry<LocalDateTime, List<Bill>> entry : temp.entrySet()) {
            // 生成 block
            blocks.add(billMapper.toBillsBlockDTO(sum(entry.getValue(), baseCoin), entry.getValue(), entry.getKey(), baseCoin));
        }
        // 对 block 进行排序
        blocks.sort(Comparator.comparing(BillsBlockDTO::getDateTime).reversed());
        ledgerRangeDetailDTO.setBlocks(blocks);
        return ledgerRangeDetailDTO;
    }


    @Override
    public LedgerRangeDetailDTO getMyBillsForDateRangeAndLedger(Long ledgerID, LocalDateTime start, LocalDateTime end) {
        Ledger ledger = ledgerService.getLedgerByID(ledgerID);
        List<Bill> bills = billRepository.findAllInDateTimeAndLedger(ledger, start, end, Sort.by("dateTime").descending());
        Coin baseCoin = ledger.getCoin(); // 本位币
        return loveYou(baseCoin,bills);
    }

    @Override
    public List<Bill> searchForKeyword(@NotBlank String keyword) {
        return billRepository.findBillsByUserEqualsAndRemarkIsLike(getMe(), "%"+keyword+"%");
    }

    @Override
    public BigDecimal sum(List<Bill> bills, Coin baseCoin) {
        return sum(bills, baseCoin.getShortName());
    }

    @Override
    public BigDecimal sum(List<Bill> bills, String baseCoin) {
        BigDecimal surplus = BigDecimal.ZERO;
        for (Bill bill : bills) {
            surplus = surplus.add(
                    currencyExchangeRateService
                            .exchange(
                                    bill.getCoin().getShortName(), bill.getAmount(), baseCoin
                            )
            );
        }
        return surplus;
    }

}
