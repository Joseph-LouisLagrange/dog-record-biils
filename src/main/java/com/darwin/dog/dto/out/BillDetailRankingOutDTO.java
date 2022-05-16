package com.darwin.dog.dto.out;

import com.darwin.dog.po.Bill;
import com.darwin.dog.po.Signory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillDetailRankingOutDTO {
    private Signory signory;
    private LocalDateTime localDateTime;
    private Bill bill;
}
