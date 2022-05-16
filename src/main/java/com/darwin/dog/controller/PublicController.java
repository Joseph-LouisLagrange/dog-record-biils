package com.darwin.dog.controller;

import com.darwin.dog.constant.BillType;
import com.darwin.dog.dto.in.CurrencyExchangeRateInDTO;
import com.darwin.dog.po.AccountType;
import com.darwin.dog.po.Coin;
import com.darwin.dog.po.LedgerCover;
import com.darwin.dog.po.Signory;
import com.darwin.dog.service.inf.*;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private LedgerCoverService ledgerCoverService;

    @Autowired
    private SignoryService signoryService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    CurrencyExchangeRateService currencyExchangeRateService;

    @PostMapping("currencyExchangeRate")
    @Transactional
    public Map<Long, Double> currencyExchangeRate(@RequestBody CurrencyExchangeRateInDTO currencyExchangeRateInDTO) {
        return currencyExchangeRateService.currencyExchangeRate(
                coinService.getOne(currencyExchangeRateInDTO.baseCoinID),
                BigDecimal.valueOf(currencyExchangeRateInDTO.amount),
                currencyExchangeRateInDTO.exchangeCoinIDs
                        .stream()
                        .map(coinService::getOne)
                        .collect(Collectors.toList())
        );
    }


    @GetMapping("/coins")
    public List<Coin> getCoins() {
        return coinService.getAll();
    }

    @GetMapping("/ledgerCovers")
    public List<LedgerCover> getLedgerCovers() {
        return ledgerCoverService.getAll();
    }

    @GetMapping("/signories")
    public List<? extends Signory> getSignories(@RequestParam("billType") Integer code) {
        return signoryService.getAll(BillType.valueOf(code));
    }

    @GetMapping("/getAccountTypes")
    public Map<String, List<AccountType>> getAccountTypes() {
        return accountTypeService.getGroupedMap();
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
