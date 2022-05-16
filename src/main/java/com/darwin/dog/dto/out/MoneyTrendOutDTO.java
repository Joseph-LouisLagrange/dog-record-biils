package com.darwin.dog.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTrendOutDTO implements Serializable {
    private Double highest = 0.00;
    private LocalDateTime highestDateTime = null;
    private Double average = 0.00;
    private Long billCount = 0L;

    private List<AmountAtDate> amountAtDates = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AmountAtDate implements Serializable{
        private Double amount;
        private String dateTime;
    }
}
