package com.darwin.dog.dto.out;

import com.darwin.dog.po.AccountType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AccountTypeOutDTO {
    Map<String, AccountType> loveYou = new HashMap<>();
}
