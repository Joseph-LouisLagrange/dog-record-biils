package com.darwin.dog.service.impl;

import com.darwin.dog.po.AccountType;
import com.darwin.dog.repository.AccountTypeRepository;
import com.darwin.dog.service.inf.AccountTypeService;
import com.darwin.dog.util.GlobalStaticBean;
import com.google.common.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AccountTypeServiceImpl implements AccountTypeService {
    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Override
    public List<AccountType> getAll() {
        return accountTypeRepository.findAll();
    }

    @Override
    public Map<String, List<AccountType>> getGroupedMap() {
        return getAll()
                .parallelStream()
                .collect(Collectors.groupingBy(AccountType::getName));
    }

    @Override
    public void importJson(String json) {
        List<AccountType> accountTypes = GlobalStaticBean.GSON.fromJson(json, new TypeToken<List<AccountType>>() {
        }.getType());
        accountTypeRepository.saveAll(accountTypes);
    }


}
