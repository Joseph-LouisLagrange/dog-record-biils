package com.darwin.dog.service.impl;

import com.darwin.dog.constant.ObjectsCode;
import com.darwin.dog.dto.in.CreateLedgerInDTO;
import com.darwin.dog.dto.in.UpdateLedgerDTO;
import com.darwin.dog.dto.mapper.LedgerMapper;
import com.darwin.dog.exception.BaseExceptionType;
import com.darwin.dog.exception.CommonException;
import com.darwin.dog.po.Bill;
import com.darwin.dog.po.Coin;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

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
    public BigDecimal surplus(long ledgerID) {
        return surplus0(getLedgerByID(ledgerID));
    }


    private BigDecimal surplus0(Ledger ledger) {
        Coin baseCoin = ledger.getCoin();
        return billService.sum(billRepository.findAllByLedgerID(ledger.getID()), baseCoin);
    }

    @Override
    public BigDecimal sumSurplusForAllLedger() {
        return ledgerRepository.findNotDeleted(getMe().getID(), Sort.unsorted())
                .parallelStream()
                .map(this::surplus0)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean update(UpdateLedgerDTO updateLedgerDTO) {
        Ledger ledger = getLedgerByID(updateLedgerDTO.getID());
        ledgerMapper.updateLedgerDTOToLedger(updateLedgerDTO, ledger);
        ledgerRepository.save(ledger);
        return true;
    }
}
