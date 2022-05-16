package com.darwin.dog.dto.in;

import com.darwin.dog.constant.BillType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBillDTO implements Serializable {
    private Long billID;
    private Long ledgerID;
    private Long signoryID;
    private String remark;
    private Double amount;
    private LocalDateTime dateTime;
    private BillType type;
    private Long coinID;
    private Long accountID;
}
