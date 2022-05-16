package com.darwin.dog.service.impl;

import com.darwin.dog.constant.BillDeleteType;
import com.darwin.dog.constant.BillType;
import com.darwin.dog.constant.ObjectsCode;
import com.darwin.dog.dto.in.CreateLedgerInDTO;
import com.darwin.dog.dto.in.UpdateLedgerDTO;
import com.darwin.dog.dto.mapper.LedgerMapper;
import com.darwin.dog.dto.out.DeletedLedgerOutDTO;
import com.darwin.dog.exception.BaseExceptionType;
import com.darwin.dog.exception.CommonException;
import com.darwin.dog.po.Bill;
import com.darwin.dog.po.Ledger;
import com.darwin.dog.po.User;
import com.darwin.dog.repository.BillRepository;
import com.darwin.dog.repository.CoinRepository;
import com.darwin.dog.repository.LedgerRepository;
import com.darwin.dog.service.inf.BillService;
import com.darwin.dog.service.inf.CurrencyExchangeRateService;
import com.darwin.dog.service.inf.LedgerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class LedgerServiceImpl implements LedgerService {

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private LedgerMapper ledgerMapper;

    @Autowired
    private CurrencyExchangeRateService currencyExchangeRateService;

    @Autowired
    private BillService billService;


    private Supplier<CommonException> ledgerMiss(String msg) {
        return () -> CommonException.of(BaseExceptionType.MISS, ObjectsCode.LEDGER, msg);
    }

    @Override
    public boolean removeCompletely(Set<Long> IDs) {
        ledgerRepository.deleteAllByIDIsIn(IDs);
        return true;
    }

    @Override
    public boolean recover(Long ID) {
        Ledger ledger = getLedgerByID(ID);
        ledger.setDeleted(false);
        ledger.getBills()
                .parallelStream()
                .forEach(bill -> bill.setDeleteType((~BillDeleteType.LEDGER_DELETE) ^ bill.getDeleteType()));
        ledgerRepository.save(ledger);
        return true;
    }

    @Override
    public boolean recover(Set<Long> IDs) {
        List<Ledger> ledgers = ledgerRepository.findAllById(IDs);
        ledgers.forEach(ledger -> {
            ledger.setDeleted(false);
            billService.reduce(ledger.getBills(), BillDeleteType.LEDGER_DELETE);
        });
        ledgerRepository.saveAll(ledgers);
        return true;
    }

    @Override
    public long queryDeletedCount() {
        return ledgerRepository.countDeleted(getMe());
    }

    @Override
    public BigDecimal sumExpense(Long ledgerID) {
        return billService.sum(billRepository.findByLedgerIDAndBillType(ledgerID, BillType.EXPENSE), getLedgerByID(ledgerID).getCoin());
    }

    @Override
    public BigDecimal sumIncome(Long ledgerID) {
        return billService.sum(billRepository.findByLedgerIDAndBillType(ledgerID, BillType.INCOME), getLedgerByID(ledgerID).getCoin());
    }

    @Override
    public Ledger getLedgerByID(long ID) {
        return ledgerRepository.findById(ID).orElseThrow(ledgerMiss(String.format("账本 ID=%d", ID)));
    }

    @Override
    public boolean create(CreateLedgerInDTO createLedgerInDTO) {
        User me = getMe();
        ledgerRepository.save(ledgerMapper.from(createLedgerInDTO, me));
        return true;
    }

    @Override
    public boolean archive(long ID) {
        ledgerRepository.updateArchiveState(ID, true);
        return true;
    }

    /**
     * 逻辑删除账本
     *
     * @param ID 欲删除的账本 ID
     * @return true 表示成功，false 表示失败
     */
    @Override
    public boolean delete(long ID) {
        try {
            // 删除账单
            billService.deleteInLedger(ID);
            // 删除账本
            ledgerRepository.safeDeleteByID(ID);
        } catch (EmptyResultDataAccessException e) {
            throw ledgerMiss(String.format("无效的删除 ID:%d", ID)).get();
        }
        return true;
    }

    @Override
    public long countBills(long ledgerID) {
        return billRepository.countByLedgerID(ledgerID);
    }

    @Override
    public long countBillsAtBillDeletedType(long ledgerID, int billDeleteType) {
        return billRepository.count((Specification<Bill>)
                (root, query, criteriaBuilder) -> query.where(
                        criteriaBuilder.equal(
                                root.get("deleteType").as(Integer.class),
                                billDeleteType),
                        criteriaBuilder.equal(
                                root.get("ledger"),
                                getLedgerByID(ledgerID))
                ).getRestriction());
    }

    @Override
    public Ledger getUsingLedger() {
        return ledgerRepository.findUsingLedger(getMe())
                .orElseThrow(ledgerMiss(String.format("userID:%d 未找到使用中的账本", getMe().getID())));
    }

    @Override
    public List<Ledger> getAll() {
        return ledgerRepository.findAll();
    }

    @Override
    public List<Ledger> getNotDeleted() {
        return ledgerRepository.findNotDeleted(getMe().getID(), Sort.by("createTime").descending());
    }

    @Override
    public List<DeletedLedgerOutDTO> queryDeleted() {
        List<Ledger> ledgers = ledgerRepository.findDeleted(
                getMe(),
                Sort.by("createTime").descending()
        );
        return ledgers.parallelStream()
                .map(ledger -> DeletedLedgerOutDTO.builder()
                        .coverUri(ledger.getCover().getUrl())
                        .billCount(countBillsAtBillDeletedType(ledger.getID(), BillDeleteType.LEDGER_DELETE))
                        .ID(ledger.getID())
                        .surplus(surplus(ledger).doubleValue())
                        .createTime(ledger.getCreateTime())
                        .name(ledger.getName())
                        .using(ledger.getUsing())
                        .build())
                .sorted(Comparator.comparing(DeletedLedgerOutDTO::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal surplus(long ledgerID) {
        return surplus(getLedgerByID(ledgerID));
    }

    @Override
    public BigDecimal surplus(Ledger ledger) {
        return billService.sum(ledger.getBills(), ledger.getCoin());
    }

    @Override
    public BigDecimal sumSurplusForAllLedger() {
        return ledgerRepository.findNotDeleted(getMe().getID(), Sort.unsorted())
                .parallelStream()
                .map(this::surplus)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean setUsingLedger(Long ledgerID) {
        Ledger usingLedger = getUsingLedger();
        Ledger ledgerByID = getLedgerByID(ledgerID);
        usingLedger.setUsing(false);
        ledgerByID.setUsing(true);
        ledgerRepository.save(usingLedger);
        ledgerRepository.save(ledgerByID);
        return false;
    }

    @Override
    public boolean update(UpdateLedgerDTO updateLedgerDTO) {
        Ledger ledger = getLedgerByID(updateLedgerDTO.getID());
        ledgerMapper.updateLedgerDTOToLedger(updateLedgerDTO, ledger);
        ledgerRepository.save(ledger);
        return true;
    }
}
