package com.darwin.dog.service.impl;

import com.darwin.dog.dto.in.AddAccountInDTO;
import com.darwin.dog.dto.mapper.AccountMapper;
import com.darwin.dog.po.Account;
import com.darwin.dog.repository.AccountRepository;
import com.darwin.dog.service.inf.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

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
        accountRepository.deleteById(ID);
        return true;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean changeBalance(Long ID, BigDecimal changeAmount) {
        Account account = accountRepository.getOne(ID);
        account.setBalance(account.getBalance().add(changeAmount));
        accountRepository.saveAndFlush(account);
        return true;
    }


    @Override
    public BigDecimal sumAssets() {
        return accountRepository.computeSumAssets(getMe()).orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal debt() {
        return accountRepository.computeDebt(getMe()).orElse(BigDecimal.ZERO);
    }
}
