package com.darwin.dog.controller;

import com.darwin.dog.constant.Coins;
import com.darwin.dog.dto.in.CreateBillInDTO;
import com.darwin.dog.dto.in.UpdateBillDTO;
import com.darwin.dog.dto.mapper.BillMapper;
import com.darwin.dog.dto.out.BillsBlockDTO;
import com.darwin.dog.po.Bill;
import com.darwin.dog.service.inf.BillService;
import com.darwin.dog.service.inf.CoinService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Slf4j
@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private CoinService coinService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/queryDeletedCount")
    public long queryDeletedCount(){
        return billService.queryDeletedCount();
    }

    @PostMapping("removeCompletely")
    public boolean removeCompletely(@RequestBody Set<Long> IDs){
        return billService.removeCompletely(IDs);
    }

    @GetMapping("/queryDeletedBillTypeCount")
    public long queryDeletedBillTypeCount(){
        return billService.queryDeletedBillTypeCount();
    }

    @GetMapping("/readDeletedBills")
    public List<BillsBlockDTO> readDeletedBills(){
        return billService.readDeletedBills();
    }

    @GetMapping("/readDeletedBillTypeBills")
    public List<BillsBlockDTO> readDeletedBillTypeBills(){
        return billService.readDeletedBillTypeBills();
    }

    @PostMapping("recover")
    public boolean recover(@RequestBody Set<Long> IDs){
        return billService.recover(IDs);
    }

    @GetMapping("/delete")
    public boolean delete(@RequestParam("ID") Long ID) {
        return billService.delete(ID);
    }

    @PostMapping("/update")
    public Bill update(@RequestBody UpdateBillDTO updateBillDTO) {
        Bill bill = billService.update(updateBillDTO);
        log.debug(bill.getSignory().toString());
        return bill;
    }

    @GetMapping("readByID")
    public Bill readByID(Long ID){
        return billService.readByID(ID);
    }

    @GetMapping("/getTodayBills")
    public BillsBlockDTO getTodayBills() {
        List<Bill> todayMyBills = billService.getTodayMyBills();
        return billMapper.toBillsBlockDTO(
                billService.sum(todayMyBills, Coins.CNY),
                todayMyBills,
                LocalDateTime.now(),
                coinService.getOne(Coins.CNY)
        );
    }

    @PostMapping("/create")
    public boolean createBill(@RequestBody CreateBillInDTO createBillInDTO) {
        return billService.createBill(createBillInDTO);
    }

    @PostMapping("/search")
    public BillsBlockDTO searchForKeyword(String keyword) {
        List<Bill> bills = billService.searchForKeyword(keyword);
        BigDecimal total = billService.sum(bills, Coins.CNY);
        return billMapper.toBillsBlockDTO(total, bills, null, coinService.getOne(Coins.CNY));
    }

    @GetMapping("/queryBillsCount")
    public long queryBillsCount(){
        return billService.queryBillsCount();
    }

}
