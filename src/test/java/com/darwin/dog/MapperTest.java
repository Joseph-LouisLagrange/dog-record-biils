package com.darwin.dog;

import com.darwin.dog.dto.mapper.LedgerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {

    @Autowired
    LedgerMapper ledgerMapper;

    @Test
    public void testLedgerMap(){

    }
}
