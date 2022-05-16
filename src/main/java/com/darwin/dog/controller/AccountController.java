package com.darwin.dog.controller;

import com.darwin.dog.dto.in.AddAccountInDTO;
import com.darwin.dog.dto.mapper.AccountMapper;
import com.darwin.dog.dto.out.AccountStatisticsDTO;
import com.darwin.dog.po.Account;
import com.darwin.dog.service.inf.AccountService;
import com.darwin.dog.service.inf.CurrencyExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CurrencyExchangeRateService currencyExchangeRateService;

    @PostMapping("removeCompletely")
    public boolean removeCompletely(@RequestBody Set<Long> IDs){
        return accountService.removeCompletely(IDs);
    }

    @PostMapping("/recover")
    public boolean recover(@RequestBody Set<Long> IDs){
        return accountService.recover(IDs);
    }


    @GetMapping("/queryDeletedCount")
    public long queryDeletedCount(){
        return accountService.queryDeletedCount();
    }

    @GetMapping("delete")
    public boolean delete(@RequestParam("ID") Long ID){
        return accountService.delete(ID);
    }

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

    @PostMapping("/update")
    public boolean updateAccount(@RequestParam("ID")Long ID,
                                 @RequestParam("name")String name,
                                 @RequestParam("remark")String remark){
        return accountService.updateAccount(ID,name,remark);
    }
}
