package com.darwin.dog.service.inf;

import com.darwin.dog.po.AccountType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AccountTypeService {
    List<AccountType> getAll();

    Map<String,List<AccountType>> getGroupedMap();

    void importJson(String json);
}
