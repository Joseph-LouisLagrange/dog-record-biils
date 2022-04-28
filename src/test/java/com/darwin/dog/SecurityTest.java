package com.darwin.dog;

import com.darwin.dog.service.PigService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;


@SpringBootTest
public class SecurityTest {

    @Autowired
    PigService pigService;

    @Test
    @WithMockUser(authorities = "TEST_ADMIN")
    public void testStaticPermission(){
        Assertions.assertThrows(AccessDeniedException.class,()-> pigService.getAll());
        Assertions.assertDoesNotThrow(()->pigService.create());
        Assertions.assertThrows(AccessDeniedException.class,()-> pigService.deleteAll());
    }


    public void initUser(){
        TestingAuthenticationToken token = new TestingAuthenticationToken(0,0);
        token.setAuthenticated(true);
        //token.setDetails(new User(Sets.newHashSet(Role.of("TEST_ADMIN")),"cc","rr","bb",null));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Test
    public void testDynamicPermission(){
        MultipartFile multipartFile;
        initUser();
        Assertions.assertDoesNotThrow(()-> pigService.getByID(1L));
        Assertions.assertThrows(AccessDeniedException.class,()->pigService.update(new long[]{1}));
    }

    public int maxProfit(int[] prices,int fee) {
        int minprice = Integer.MAX_VALUE;
        int maxprofit = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < minprice) {
                minprice = prices[i];
            } else if (prices[i] - minprice > maxprofit) {
                maxprofit = prices[i] - minprice;
            }
        }
        return maxprofit;
    }
}
