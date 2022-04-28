package com.darwin.dog.dto.out;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AccountStatisticsDTO {
    @ApiModelProperty("总资产")
    private Double sumAssets;
    @ApiModelProperty("净资产")
    private Double netAssets;;
    @ApiModelProperty("负债")
    private Double debt;
}
