package com.darwin.dog.service.inf;

import com.darwin.dog.constant.BillType;
import com.darwin.dog.po.Signory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SignoryService {

    void importJsonForIncome(String json);

    void importJsonForExpense(String json);

    List<? extends Signory> getAll(BillType billType);
}
