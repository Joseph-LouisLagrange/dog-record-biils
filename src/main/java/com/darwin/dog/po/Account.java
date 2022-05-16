package com.darwin.dog.po;

import com.darwin.dog.annotation.Comment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Entity
@Comment("用户账户")
@Table(name = "account")
public class Account implements Serializable {

    @Id
    @Comment("主键 id")
    @JsonProperty("ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false,updatable = false,insertable = false,columnDefinition = "bigint UNSIGNED")
    private long ID;

    @NonNull
    @ManyToOne(targetEntity = AccountType.class,cascade = CascadeType.ALL,optional = false)
    @JoinColumn(referencedColumnName = "id",name = "type_id")
    private AccountType type;

    @NonNull
    @Comment("账户名称")
    @Column(name = "name",columnDefinition = "VARCHAR(128)")
    private String name;

    @NonNull
    @Comment("账户余额")
    @Column(name = "balance",columnDefinition = "DECIMAL(10,2)")
    private BigDecimal balance;

    @NonNull
    @Comment("账户备注")
    @Column(name = "remark",columnDefinition = "VARCHAR(128)")
    private String remark;

    @NonNull
    @Comment("创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @NonNull
    @ManyToOne(targetEntity = Coin.class,cascade = CascadeType.PERSIST,optional = false)
    @JoinColumn(referencedColumnName = "id",name = "coin_id")
    private Coin coin;

    @NonNull
    @JsonIgnore
    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    private User user;

    @NonNull
    @JsonIgnore
    @OneToMany(targetEntity = Bill.class,cascade = CascadeType.ALL,mappedBy = "account")
    private List<Bill> bills = new ArrayList<>();

    @NonNull
    @Comment("删除标记")
    @Column(name = "deleted",columnDefinition = "INT(1) NOT NULL DEFAULT 0")
    private Boolean deleted;

}
