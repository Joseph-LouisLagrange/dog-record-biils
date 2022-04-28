package com.darwin.dog.po;

import com.darwin.dog.annotation.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Entity
@Comment("账本封面表")
@Table(name = "ledger_cover")
public class LedgerCover {
    @Id
    @JsonProperty("ID")
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("主键 id")
    @Column(name = "id",unique = true,nullable = false,columnDefinition = "bigint UNSIGNED")
    private long ID;

    @NonNull
    @Comment("封面名称")
    @Column(name = "name",columnDefinition = "VARCHAR(128)")
    private String name;

    @NonNull
    @Comment("封面资源 URL")
    @Column(name = "url",columnDefinition = "VARCHAR(255)")
    private String url;
}
