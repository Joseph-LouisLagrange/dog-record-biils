package com.darwin.dog;


import com.darwin.dog.dto.in.AddAccountInDTO;
import com.darwin.dog.dto.mapper.AccountMapper;
import com.darwin.dog.po.Account;
import com.darwin.dog.po.AccountType;
import com.darwin.dog.repository.AccountRepository;
import com.darwin.dog.repository.AccountTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@SpringBootTest
@Transactional
class DogRecordBillsApplicationTests {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountTypeRepository accountTypeRepository;

    @Autowired
    AccountMapper accountMapper;

    @Test
    @Rollback(value = true)
    public void test(){

    }
}
