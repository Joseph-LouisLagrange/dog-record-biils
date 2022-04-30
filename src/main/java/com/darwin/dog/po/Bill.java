package com.darwin.dog.po;

import com.darwin.dog.annotation.Comment;
import com.darwin.dog.constant.BillDeleteType;
import com.darwin.dog.constant.BillType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Entity
@Comment("账单")
@Table(name = "bill")
public class Bill implements Serializable {

    @Id
    @Comment("主键 id")
    @JsonProperty("ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false, insertable = false, columnDefinition = "bigint UNSIGNED")
    private long ID;

    @NonNull
    @JsonIgnore
    @ManyToOne(targetEntity = Ledger.class, cascade = CascadeType.ALL
            , optional = false)
    @JoinColumn(name = "ledger_id", referencedColumnName = "id")
    private Ledger ledger;    // 所属账本

    @NonNull
    @ManyToOne(targetEntity = Signory.class, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(referencedColumnName = "id", name = "signory_id")
    private Signory signory;    // 领域

    @NonNull
    @Comment("账单备注")
    @Column(name = "remark", columnDefinition = "VARCHAR(128)")
    private String remark;

    @NonNull
    @Comment("金额")
    @Column(name = "amount", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal amount;

    @NonNull
    @Comment("记账日期")
    @Column(name = "date_time", columnDefinition = "DATETIME")
    private LocalDateTime dateTime;

    @NonNull
    @Comment("账单类型")
    @Column(name = "type")
    private BillType type;


    @NonNull
    @Comment("删除标记")
    @Column(name = "delete_state")
    private BillDeleteType deleteType;

    @NonNull
    @ManyToOne(targetEntity = Coin.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(referencedColumnName = "id", name = "coin_id")
    private Coin coin;    // 所用货币

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "id",name = "account_id")
    private Account account;    // 所属账户(可以为 null 表示不计入账户)

    @NonNull
    @ManyToOne(targetEntity = User.class)
    @JsonIgnore
    @JoinColumn(referencedColumnName = "id",name = "user_id")
    private User user;    // 关联用户
}
