package com.darwin.dog.dto.out;

import com.darwin.dog.po.Signory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("领域占比")
public class MoneySignoryPartOutDTO {
    @ApiModelProperty("领域")
    public Signory signory;
    @ApiModelProperty("百分率")
    public Double percent;
}
