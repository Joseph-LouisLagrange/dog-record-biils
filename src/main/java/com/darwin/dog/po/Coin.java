package com.darwin.dog.po;

import com.darwin.dog.annotation.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Entity
@Comment("货币表")
@Table(name = "coin")
public class Coin implements Serializable {

    @Id
    @JsonProperty("ID")
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("主键 id")
    @Column(name = "id",unique = true,nullable = false,columnDefinition = "bigint UNSIGNED")
    private long ID;

    @NonNull
    @Basic(fetch = FetchType.EAGER)
    @Comment("货币名称")
    @Column(name = "name",columnDefinition = "VARCHAR(128)")
    private String name;

    @NonNull
    @Basic(fetch = FetchType.EAGER)
    @Comment("货币的英文缩写简称")
    @Column(name = "short_name",columnDefinition = "CHAR(4)",unique = true,nullable = false)
    private String shortName;

    @NonNull
    @Basic(fetch = FetchType.EAGER)
    @Comment("货币标识")
    @Column(name = "symbol",columnDefinition = "CHAR(10)",unique = true,nullable = false)
    private String symbol;

    @NonNull
    @Basic(fetch = FetchType.EAGER)
    @Comment("icon_url")
    @Column(name = "icon_url")
    private String iconUrl;

}
