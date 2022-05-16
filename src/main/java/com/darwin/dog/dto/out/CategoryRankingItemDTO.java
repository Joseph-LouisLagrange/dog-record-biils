package com.darwin.dog.dto.out;


import com.darwin.dog.po.Signory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CategoryRankingItemDTO {
    private Signory signory;
    private Double money;
    private Long billsCount;

    public Double getMoney() {
        return money;
    }
}
