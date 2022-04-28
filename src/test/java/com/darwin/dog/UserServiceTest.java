package com.darwin.dog;

import com.darwin.dog.dto.in.RegisteringUserInDTO;
import com.darwin.dog.service.inf.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.MimeTypeUtils;

import java.nio.charset.StandardCharsets;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Test
    @Rollback(value = false)
    public void testRegister(){
        Assertions.assertDoesNotThrow(()->{
            userService.register(RegisteringUserInDTO.of(
                    RandomStringUtils.randomAlphanumeric(5),
                    RandomStringUtils.randomNumeric(10),
                    "123456",
                    new MockMultipartFile(
                            "file",
                            RandomStringUtils.randomAlphabetic(3),
                            String.format("%s; charset=%s",MimeTypeUtils.IMAGE_PNG_VALUE, StandardCharsets.UTF_8.name()),
                            RandomStringUtils.random(10).getBytes(StandardCharsets.UTF_8)
                    )
            ));
        });
    }
}
