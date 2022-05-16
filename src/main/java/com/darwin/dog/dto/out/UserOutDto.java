package com.darwin.dog.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutDto implements Serializable {
    private long ID;
    private String nickName;
    private String username;
    private String password;
    private String avatarUrl;
}
