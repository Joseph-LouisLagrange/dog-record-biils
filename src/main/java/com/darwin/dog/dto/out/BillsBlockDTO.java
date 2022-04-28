package com.darwin.dog.dto.out;

import com.darwin.dog.po.Bill;
import com.darwin.dog.po.Coin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel
public class BillsBlockDTO implements Serializable {
    @ApiModelProperty("账单列表")
    private List<Bill> bills;
    @ApiModelProperty("总计")
    private Double total;
    @ApiModelProperty("日期")
    private LocalDateTime dateTime;
    @ApiModelProperty("本位币")
    private Coin coin;
}
