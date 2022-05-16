package com.darwin.dog.dto.in;

import com.darwin.dog.constant.BillType;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class QueryRangesWithBillTypeInDTO {
    @ApiModelProperty("查询范围序列")
    public List<Range> ranges;
    public String mode;
    public BillType billType;
}
