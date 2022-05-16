package com.darwin.dog.dto.out;

import com.darwin.dog.po.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AssetsRangeDetailDTO implements Serializable {
    private double inflow;
    private double outflow;
    private Coin coin;
    private List<BillsBlockDTO> billsBlockDTOS;
}
