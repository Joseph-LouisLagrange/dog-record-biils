package com.darwin.dog.controller;

import com.darwin.dog.annotation.NoDto;
import com.darwin.dog.dto.in.CreateLedgerInDTO;
import com.darwin.dog.dto.in.QueryRangeInDTO;
import com.darwin.dog.dto.in.UpdateLedgerDTO;
import com.darwin.dog.dto.mapper.LedgerMapper;
import com.darwin.dog.dto.out.LedgerDetailDTO;
import com.darwin.dog.dto.out.LedgerRangeDetailDTO;
import com.darwin.dog.dto.out.ListLedgerOutDTO;
import com.darwin.dog.po.Ledger;
import com.darwin.dog.service.inf.BillService;
import com.darwin.dog.service.inf.LedgerService;
import com.darwin.dog.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/ledger")
public class LedgerController {
    @Autowired
    LedgerService ledgerService;

    @Autowired
    UserService userService;

    @Autowired
    LedgerMapper ledgerMapper;

    @Autowired
    BillService billService;

    @PostMapping("/create")
    public boolean create(@RequestBody CreateLedgerInDTO createLedgerInDTO){
        return ledgerService.create(createLedgerInDTO);
    }

    @GetMapping("/delete")
    public boolean delete(@RequestParam("ID") Long ID){
        return ledgerService.delete(ID);
    }

    @GetMapping("/readAll")
    public ListLedgerOutDTO readAll(){
        return ledgerMapper.to(ledgerService.sumSurplusForAllLedger(),ledgerService.getNotDeleted());
    }

    @GetMapping("/readUsingLedger")
    public Ledger readUsingLedger(){
        return ledgerService.getUsingLedger();
    }

    @GetMapping("/readByID")
    public LedgerDetailDTO readByID(@RequestParam("ID") long ID){
        return ledgerMapper.ledgerToLedgerDetailDTO(ledgerService.getLedgerByID(ID));
    }

    @PostMapping("/update")
    public boolean update(@RequestBody UpdateLedgerDTO updateLedgerDTO){
        return ledgerService.update(updateLedgerDTO);
    }

    @PostMapping("/readLedgerForDateRange")
    public LedgerRangeDetailDTO readLedgerForDateRange(@RequestParam("start")LocalDateTime start,
                                                       @RequestParam("end")LocalDateTime end,
                                                       @RequestParam("ledgerID") Long ledgerID){
        return billService.getMyBillsForDateRangeAndLedger(ledgerID, start, end);
    }

    
    @PostMapping("/readLedgerForDateRanges")
    public LedgerRangeDetailDTO readLedgerForDateRanges(@RequestBody QueryRangeInDTO queryRangeInDTO){
        return billService.readLedgerForDateRanges(queryRangeInDTO);
    }


}
