package com.darwin.dog.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 创建账本 DTO
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateLedgerInDTO {

    private String name;

    private Long coinID;

    private Long coverID;

    private boolean using;
}
