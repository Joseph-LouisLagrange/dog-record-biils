package com.darwin.dog.po.sys;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 角色实体
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Table(name = "role")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,updatable = false,columnDefinition = "BIGINT UNSIGNED")
    private long ID;

    @NonNull
    @Column(name = "name",updatable = false,columnDefinition = "VARCHAR(128)")
    private String name;

    @ManyToMany(targetEntity = Permission.class,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Permission> permissions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return Objects.equals(ID, role.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name);
    }
}
