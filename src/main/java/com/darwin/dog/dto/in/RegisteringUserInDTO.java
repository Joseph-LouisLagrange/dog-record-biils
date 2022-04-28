package com.darwin.dog.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * user 注册的请求 DTO
 */
@Data
@AllArgsConstructor(staticName = "of")
public class RegisteringUserInDTO {
    private String nickName;

    private String username;

    private String password;

    private MultipartFile avatar;
}
