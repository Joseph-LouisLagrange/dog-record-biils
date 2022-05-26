package com.darwin.dog;

import antlr.collections.impl.IntRange;
import com.darwin.dog.po.Ledger;
import com.darwin.dog.repository.LedgerRepository;
import com.darwin.dog.service.inf.CurrencyExchangeRateService;
import com.darwin.dog.service.inf.LedgerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
//@WithUserDetails(value = "5335361060")
public class SimpleTest {

    @Autowired
    CurrencyExchangeRateService currencyExchangeRateService;

    @Autowired
    LedgerService ledgerService;

    @Test
    public void test() {

    }
}
