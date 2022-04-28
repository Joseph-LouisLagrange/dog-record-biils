package com.darwin.dog.po.sys;

import com.darwin.dog.annotation.Comment;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Entity
@Comment("文件")
@Table(name = "file_plan")
public class FilePlan {
    @Id
    @Comment("主键 id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false,updatable = false,insertable = false,columnDefinition = "bigint UNSIGNED")
    private long ID;

    @NonNull
    @Comment("文件名")
    @NotEmpty
    @Column(name = "name",columnDefinition = "VARCHAR(128)")
    private String name;

    @NonNull
    @Comment("文件路径")
    @NotEmpty
    @Column(name = "path",columnDefinition = "CHAR(255)")
    private String path;

    @NonNull
    @Comment("文件的创建时间")
    @Column(name = "create_date_time",columnDefinition = "DATETIME")
    private LocalDateTime createDateTime;

    @NonNull
    @Comment("文件大小")
    @PositiveOrZero
    @Column(name = "size",columnDefinition = "bigint UNSIGNED")
    private Long size;


    @Comment("文件类型")
    @Column(name = "type",columnDefinition = "VARCHAR(64)")
    private String type;

    @Comment("文件编码")
    @Column(name = "charset",columnDefinition = "VARCHAR(64)")
    private String charset;
}
