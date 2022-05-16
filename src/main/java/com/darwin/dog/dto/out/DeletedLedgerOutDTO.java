package com.darwin.dog.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletedLedgerOutDTO implements Serializable {
    @JsonProperty("ID")
    private Long ID;
    private String name;
    private String coverUri;
    private LocalDateTime createTime;
    private Boolean using;

    public double surplus;

    public long billCount;
}
