package com.darwin.dog.dto.in;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ApiModel
public class AddAccountInDTO {

    @ApiModelProperty("类型 ID")
    private long typeID;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("余额")
    private Double balance;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("货币 ID")
    private long coinID;
}
