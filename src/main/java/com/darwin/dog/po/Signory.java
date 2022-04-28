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
@Comment("账单的使用领域")
@Table(name = "signory")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type",discriminatorType = DiscriminatorType.INTEGER)
public class Signory implements Serializable {
    @Id
    @Comment("主键 id")
    @JsonProperty("ID")
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false, insertable = false, columnDefinition = "bigint UNSIGNED")
    private long ID;

    @NonNull
    @Comment("领域名称")
    @Column(name = "name", columnDefinition = "CHAR(3)")
    private String name;

    @NonNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type",column = @Column(name = "icon_type")),
            @AttributeOverride(name = "name",column = @Column(name = "icon_name"))
    })
    private Icon icon;

    @Data
    @Embeddable
    private static class Icon {
        @Column(name = "type")
        private String type;
        @Column(name = "name")
        private String name;
    }

}
