package com.darwin.dog.dto.out;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AmountDTO {
    private Double surplus;
    private Double expense;
    private Double income;
}
