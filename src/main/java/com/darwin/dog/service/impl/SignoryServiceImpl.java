package com.darwin.dog.service.impl;

import com.darwin.dog.constant.BillType;
import com.darwin.dog.po.ExpenseSignory;
import com.darwin.dog.po.IncomeSignory;
import com.darwin.dog.po.Signory;
import com.darwin.dog.repository.ExpenseSignoryRespository;
import com.darwin.dog.repository.IncomeSignoryRespository;
import com.darwin.dog.repository.SignoryRepository;
import com.darwin.dog.service.inf.SignoryService;
import com.darwin.dog.util.GlobalStaticBean;
import com.google.common.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Cacheable(cacheNames = "signory")
public class SignoryServiceImpl implements SignoryService {

    @Autowired
    private IncomeSignoryRespository incomeSignoryRespository;

    @Autowired
    private ExpenseSignoryRespository expenseSignoryRespository;

    @Override
    public void importJsonForIncome(String json) {
        List<IncomeSignory> signories =  GlobalStaticBean.GSON.fromJson(json,new TypeToken<List<IncomeSignory>>(){}.getType());
        incomeSignoryRespository.saveAll(signories);
    }

    @Override
    public void importJsonForExpense(String json) {
        List<ExpenseSignory> signories =  GlobalStaticBean.GSON.fromJson(json,new TypeToken<List<ExpenseSignory>>(){}.getType());
        expenseSignoryRespository.saveAll(signories);
    }

    @Override
    public List<? extends Signory> getAll(BillType billType) {
        if (BillType.EXPENSE == billType){
            return expenseSignoryRespository.findAll();
        }
        return incomeSignoryRespository.findAll();
    }
}
