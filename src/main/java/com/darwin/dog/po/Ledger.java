package com.darwin.dog.po;

import com.darwin.dog.annotation.Comment;
import com.darwin.dog.po.sys.FilePlan;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Entity
@Comment("账本")
@DynamicUpdate
@Table(name = "ledger")
public class Ledger implements Serializable {

    @Id
    @JsonProperty("ID")
    @Comment("主键 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false,updatable = false,insertable = false,columnDefinition = "bigint UNSIGNED")
    private Long ID;

    @NonNull
    @Comment("账本名称")
    @Column(name = "name",columnDefinition = "VARCHAR(128)")
    private String name;

    @NonNull
    @ManyToOne(targetEntity = LedgerCover.class,optional = false,cascade = CascadeType.REFRESH)
    @JoinColumn(referencedColumnName = "id", name = "cover_id")
    private LedgerCover cover;

    @NonNull
    @Comment("创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @NonNull
    @Comment("是否正在使用中")
    @Column(name = "`using`")
    private Boolean using;

    @NonNull
    @Comment("是否归档")
    @Column(name = "archive")
    private Boolean archive;

    @NonNull
    @Comment("删除标记")
    @Column(name = "deleted",columnDefinition = "INT(1) NOT NULL DEFAULT 0")
    private Boolean deleted;


    @NonNull
    @JsonIgnore
    @ManyToOne(targetEntity = User.class,optional = false,cascade = CascadeType.REFRESH)
    @JoinColumn(referencedColumnName = "id",nullable = false,name = "user_id")
    private User user;

    @NonNull
    @JsonIgnore
    @OneToMany(targetEntity = Bill.class,mappedBy = "ledger",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Bill> bills;

    @NonNull
    @ManyToOne(targetEntity = Coin.class,optional = false,cascade = CascadeType.REFRESH)
    @JoinColumn(referencedColumnName = "id",nullable = false,
    name = "coin_id")
    private Coin coin;

}
