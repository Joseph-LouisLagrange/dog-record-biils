package com.darwin.dog.dto.out;

import com.darwin.dog.po.Coin;
import com.darwin.dog.po.LedgerCover;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel
public class LedgerDetailDTO implements Serializable {
    @JsonProperty("ID")
    private Long ID;
    private String name;
    private LedgerCover cover;
    private LocalDateTime createTime;
    private Boolean using;
    private Boolean archive;
    private Boolean deleted;
    private Coin coin;

    @ApiModelProperty("总结余")
    private Double surplus;
    @ApiModelProperty("账单数量")
    private Long billCount;
}
