package com.darwin.dog.dto.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QueryRangeAtAccountInDTO implements Serializable {
    @ApiModelProperty("查询范围序列")
    public List<Range> ranges;
    @ApiModelProperty("账本 ID")
    public Long accountID;
}
