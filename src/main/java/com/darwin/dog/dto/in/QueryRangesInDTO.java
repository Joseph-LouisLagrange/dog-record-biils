package com.darwin.dog.dto.in;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class QueryRangesInDTO {
    @ApiModelProperty("查询范围序列")
    public List<Range> ranges;
}
