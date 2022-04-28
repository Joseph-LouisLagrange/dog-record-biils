package com.darwin.dog.dto.out;

import com.darwin.dog.po.Coin;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class ListLedgerOutDTO {
    @ApiModelProperty("总结余")
    public double sumSurplus;

    @ApiModelProperty("账本列表")
    public List<LedgerOutDTO> ledgers;

    @Data
    public static class LedgerOutDTO {
        @JsonProperty("ID")
        @ApiModelProperty("账本 ID")
        public Long ID;
        @ApiModelProperty("账本名称")
        public String name;
        @ApiModelProperty("账本结余")
        public double surplus;
        @ApiModelProperty("记录账单数量")
        public long billCount;
        @ApiModelProperty("是否正在使用中")
        public boolean using;
        @ApiModelProperty("封面的Uri")
        public String coverUri;
        @ApiModelProperty("账本默认的货币")
        public Coin coin;
    }
}
