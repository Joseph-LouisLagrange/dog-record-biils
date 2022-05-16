package com.darwin.dog.dto.out;

import com.darwin.dog.po.Coin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@Builder
public class LedgerRangeDetailDTO implements Serializable {
    @ApiModelProperty("结余")
    public Double surplus;
    @ApiModelProperty("支出")
    public Double expense;
    @ApiModelProperty("收入")
    public Double income;
    @ApiModelProperty("默认货币")
    public Coin coin;
    @ApiModelProperty("账单块列表")
    List<BillsBlockDTO> blocks;
}
