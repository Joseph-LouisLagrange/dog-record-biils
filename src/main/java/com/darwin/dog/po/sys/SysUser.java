package com.darwin.dog.po.sys;

import com.darwin.dog.annotation.Comment;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Comment("用户表")
@Table(name = "sys_user")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class SysUser implements Serializable {

    @Id
    @Comment("用户 id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false,updatable = false,insertable = false,columnDefinition = "bigint UNSIGNED")
    protected long ID;

    @NonNull
    @ManyToMany(targetEntity = Role.class,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    protected Set<Role> roles = new HashSet<>();

    @NonNull
    @CreatedDate
    @Comment("创建时间")
    protected LocalDateTime createDateTime;

    public SysUser(@NonNull Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SysUser sysUser = (SysUser) o;
        return Objects.equals(ID, sysUser.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
