package com.darwin.dog.service;

import com.darwin.dog.dto.in.CreateLedgerInDTO;
import com.darwin.dog.dto.in.UpdateLedgerDTO;
import com.darwin.dog.po.Ledger;
import com.darwin.dog.po.User;
import com.darwin.dog.repository.LedgerRepository;
import com.darwin.dog.service.inf.LedgerService;
import com.darwin.dog.service.inf.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;


@SpringBootTest
@WithUserDetails(value = "5335361060")
public class LedgerServiceTest {


    @Autowired
    LedgerRepository ledgerRepository;

    @Autowired
    LedgerService ledgerService;

    @Test
    public void testCreate(){
        CreateLedgerInDTO createLedgerInDTO = new CreateLedgerInDTO(RandomStringUtils.randomAlphanumeric(5),
                1L, 1L, false);
        Assertions.assertTrue(ledgerService.create(createLedgerInDTO));
    }

    @Test
    public void testSurplus(){
        Mockito.when(ledgerRepository.computeSurplus(Mockito.anyLong()))
                .thenReturn(Optional.of(BigDecimal.TEN));
        Assertions.assertEquals(ledgerService.surplus(1),BigDecimal.TEN);
    }

    @Test
    public void readAll(){

    }

    @Test
    public void update(){

    }
}
