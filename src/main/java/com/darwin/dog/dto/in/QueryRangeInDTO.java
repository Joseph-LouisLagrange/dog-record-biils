package com.darwin.dog.dto.in;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel
public class QueryRangeInDTO implements Serializable {
    @ApiModelProperty("查询范围序列")
    public List<Range> ranges;
    @ApiModelProperty("账本 ID")
    public Long ledgerID;

    @Data
    public static class Range {
        @ApiModelProperty("开始时间")
        public LocalDateTime startDateTime;
        @ApiModelProperty("结束时间")
        public LocalDateTime endDateTime;
    }
}
