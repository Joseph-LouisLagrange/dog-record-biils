package com.darwin.dog.controller;

import com.darwin.dog.constant.Coins;
import com.darwin.dog.dto.in.CreateBillInDTO;
import com.darwin.dog.dto.mapper.BillMapper;
import com.darwin.dog.dto.out.BillsBlockDTO;
import com.darwin.dog.po.Bill;
import com.darwin.dog.service.inf.BillService;
import com.darwin.dog.service.inf.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private CoinService coinService;

    @GetMapping("/getTodayBills")
    public BillsBlockDTO getTodayBills(){
        List<Bill> todayMyBills = billService.getTodayMyBills();
        return billMapper.toBillsBlockDTO(billService.sum(todayMyBills, Coins.CNY),todayMyBills, LocalDateTime.now(),coinService.getOne(Coins.CNY));
    }

    @PostMapping("/create")
    public boolean createBill(@RequestBody CreateBillInDTO createBillInDTO){
        return billService.createBill(createBillInDTO);
    }

    @PostMapping("/search")
    public BillsBlockDTO searchForKeyword(String keyword){
        List<Bill> bills = billService.searchForKeyword(keyword);
        BigDecimal total = billService.sum(bills, Coins.CNY);
        return billMapper.toBillsBlockDTO(total,bills,null,coinService.getOne(Coins.CNY));
    }
}
