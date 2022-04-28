package com.darwin.dog.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateLedgerDTO implements Serializable {
    @JsonProperty("ID")
    private Long ID;
    private String name;
    private long coverID;
    private long coinID;
    private Boolean using;
    private Boolean archive;
    private Boolean deleted;
}
