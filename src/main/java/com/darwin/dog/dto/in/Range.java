package com.darwin.dog.dto.in;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;

public class Range {
    @ApiModelProperty("开始时间")
    public LocalDateTime startDateTime;
    @ApiModelProperty("结束时间")
    public LocalDateTime endDateTime;
}
