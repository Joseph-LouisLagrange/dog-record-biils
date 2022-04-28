package com.darwin.dog.po;

import com.darwin.dog.annotation.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Entity
@Comment("账户类型")
@Table(name = "account_type")
public class AccountType {

    @Id
    @Comment("主键 id")
    @JsonProperty("ID")
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false,updatable = false,insertable = false,columnDefinition = "bigint UNSIGNED")
    private long ID;

    @NonNull
    @Comment("类型名称")
    @Column(name = "name",columnDefinition = "VARCHAR(128)")
    private String name;

    @NonNull
    @Comment("账户的具体源")
    @Column(name = "origin")
    private String origin;
}
