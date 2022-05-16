package com.darwin.dog.controller;

import com.darwin.dog.dto.in.QueryRangeAtAccountInDTO;
import com.darwin.dog.dto.in.QueryRangesInDTO;
import com.darwin.dog.dto.in.QueryRangesWithBillTypeInDTO;
import com.darwin.dog.dto.out.*;
import com.darwin.dog.po.Bill;
import com.darwin.dog.service.inf.BillService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;
import java.util.List;

@RestController
@RequestMapping("statistics")
public class StatisticsController {

    @Autowired
    BillService billService;

    @PostMapping("readBillsInAccount")
    public AssetsRangeDetailDTO readBillsInAccount(@RequestBody QueryRangeAtAccountInDTO queryRangeAtAccountInDTO){
        return billService.readBillsInAccount(queryRangeAtAccountInDTO);
    }

    @PostMapping("countAmount")
    public AmountDTO countAmount(@RequestBody QueryRangesInDTO queryRangesInDTO){
        return billService.countMyAmount(queryRangesInDTO);
    }

    @PostMapping("countMoneyTrend")
    public MoneyTrendOutDTO countMoneyTrend(@RequestBody QueryRangesWithBillTypeInDTO queryRangesInDTO){
        return billService.countMoneyTrend(queryRangesInDTO);
    }

    @PostMapping("countMoneySignoryPart")
    public List<MoneySignoryPartOutDTO> countMoneySignoryPart(@RequestBody QueryRangesWithBillTypeInDTO queryRangesWithBillTypeInDTO){
        return billService.countMoneySignoryPart(queryRangesWithBillTypeInDTO);
    }

    @PostMapping("countCategoryRanking")
    public List<CategoryRankingItemDTO> countCategoryRanking(@RequestBody QueryRangesWithBillTypeInDTO queryRangesWithBillTypeInDTO){
       return billService.countCategoryRanking(queryRangesWithBillTypeInDTO);
    }

    @PostMapping("countBillRanking")
    public List<Bill> countBillRanking(@RequestBody QueryRangesWithBillTypeInDTO queryRangesWithBillTypeInDTO){
        return billService.countBillRanking(queryRangesWithBillTypeInDTO);
    }
}
