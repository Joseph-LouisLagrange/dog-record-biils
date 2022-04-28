package com.darwin.dog.dto.in;

import com.darwin.dog.constant.BillType;
import com.darwin.dog.po.Coin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel
public class CreateBillInDTO {
    @ApiModelProperty("所属账本ID")
    public Long ledgerID;
    @ApiModelProperty("领域ID")
    public Long signoryID;
    @ApiModelProperty("备注")
    public String remark;
    @ApiModelProperty("金额")
    public Double amount;
    @ApiModelProperty(value = "类型"
            ,notes = "收入或者支出类型的枚举定义值")
    public BillType type;
    @ApiModelProperty("货币 ID")
    public Long coinID;
    @ApiModelProperty(value = "账号 ID",allowEmptyValue = true)
    public Long accountID;
    @ApiModelProperty("账单时间")
    public LocalDateTime dateTime;
}
