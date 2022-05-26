package com.darwin.dog.service.impl;

import com.darwin.dog.constant.BillDeleteType;
import com.darwin.dog.constant.Coins;
import com.darwin.dog.dto.in.AddAccountInDTO;
import com.darwin.dog.dto.mapper.AccountMapper;
import com.darwin.dog.po.Account;
import com.darwin.dog.po.Coin;
import com.darwin.dog.repository.AccountRepository;
import com.darwin.dog.service.inf.AccountService;
import com.darwin.dog.service.inf.BillService;
import com.darwin.dog.service.inf.CurrencyExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BillService billService;

    @Autowired
    private CurrencyExchangeRateService currencyExchangeRateService;

    @Override
    public List<Account> queryDeletedAccounts() {
        return accountRepository.findAccountsByUserAndDeleted(getMe(), true);
    }

    @Override
    public boolean removeCompletely(Set<Long> IDs) {
        accountRepository.deleteAll(accountRepository.findAllById(IDs));
        return true;
    }

    @Override
    public boolean recover(Long ID) {
        Account account = getOne(ID);
        account.setDeleted(false);
        account.getBills()
                .parallelStream()
                .forEach(bill -> bill.setDeleteType((~BillDeleteType.ACCOUNT_DELETE) & bill.getDeleteType())
                );
        accountRepository.save(account);
        return true;
    }

    @Override
    public boolean recover(Set<Long> IDs) {
        List<Account> accounts = accountRepository.findAllById(IDs);
        accounts
                .forEach(account -> {
                    account.setDeleted(false);
                    account.getBills()
                            .parallelStream()
                            .forEach(bill -> bill.setDeleteType((~BillDeleteType.ACCOUNT_DELETE) & bill.getDeleteType())
                            );
                });
        accountRepository.saveAll(accounts);
        return true;
    }

    @Override
    public long queryDeletedCount() {
        return accountRepository.countDeleted(getMe());
    }

    @Override
    public boolean updateAccount(long ID, String name, String remark) {
        Account account = accountRepository.getOne(ID);
        account.setName(name);
        account.setRemark(remark);
        accountRepository.save(account);
        return true;
    }

    @Override
    public boolean add(AddAccountInDTO addAccountInDTO) {
        Account account = accountMapper.from(addAccountInDTO, getMe());
        accountRepository.save(account);
        return true;
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findMyAll(getMe());
    }

    @Override
    public boolean delete(Long ID) {
        accountRepository.safeDelete(ID);
        billService.deleteInAccount(ID);
        return true;
    }

    @Override
    public boolean update(Account account) {
        accountRepository.save(account);
        return true;
    }

    @Override
    public synchronized boolean addBalance(Long ID, Coin coin, BigDecimal changeAmount) {
        Account account = accountRepository.getOne(ID);
        // 汇率换算
        changeAmount = currencyExchangeRateService.exchange(
                coin,
                changeAmount,
                account.getCoin());
        account.setBalance(account.getBalance().add(changeAmount));
        accountRepository.saveAndFlush(account);
        return true;
    }

    @Override
    public synchronized boolean subtractBalance(Long ID, Coin coin, BigDecimal changeAmount) {
        Account account = accountRepository.getOne(ID);
        // 汇率换算
        changeAmount = currencyExchangeRateService.exchange(
                coin,
                changeAmount,
                account.getCoin());
        account.setBalance(account.getBalance().subtract(changeAmount));
        accountRepository.saveAndFlush(account);
        return true;
    }

    @Override
    public BigDecimal sumAssets() {
        return accountRepository
                .findAccountsByUserAndBalanceGreaterThanAndAndDeleted(
                        getMe(),
                        BigDecimal.ZERO,
                        false)
                .parallelStream()
                .map(account -> currencyExchangeRateService.exchange(
                        account.getCoin().getShortName(),
                        account.getBalance(),
                        Coins.CNY
                ))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal debt() {
        return accountRepository
                .findAccountsByUserAndBalanceLessThanAndDeleted(
                        getMe(),
                        BigDecimal.ZERO,
                        false)
                .parallelStream()
                .map(account -> currencyExchangeRateService.exchange(
                        account.getCoin().getShortName(),
                        account.getBalance(),
                        Coins.CNY
                ))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }


    @Override
    public Account getOne(Long ID) {
        return accountRepository.getOne(ID);
    }
}
