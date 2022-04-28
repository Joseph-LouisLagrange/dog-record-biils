package com.darwin.dog.controller;

import com.darwin.dog.dto.in.AddAccountInDTO;
import com.darwin.dog.dto.mapper.AccountMapper;
import com.darwin.dog.dto.out.AccountStatisticsDTO;
import com.darwin.dog.po.Account;
import com.darwin.dog.service.inf.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @PostMapping("/create")
    public boolean create(@RequestBody AddAccountInDTO addAccountInDTO){
        return accountService.add(addAccountInDTO);
    }

    @GetMapping("/readAll")
    public List<Account> readAll(){
        return accountService.getAll();
    }

    @GetMapping("/getAccountStatistics")
    public AccountStatisticsDTO getAccountStatistics(){
        return accountMapper.toAccountStatisticsDTO(accountService.sumAssets(),accountService.debt());
    }

    public boolean updateAccount(){
        return true;
    }
}
