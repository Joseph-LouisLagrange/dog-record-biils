package com.darwin.dog.po;

import com.darwin.dog.po.sys.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "user")
public class User extends com.darwin.dog.po.sys.SysUser implements UserDetails {

    @NonNull
    @Column(name = "nick_name",columnDefinition = "VARCHAR(128)")
    private String nickName;

    @NonNull
    @Column(name = "username",columnDefinition = "CHAR(10)")
    private String username;

    @NonNull
    @Column(name = "password",columnDefinition = "VARCHAR(128)")
    private String password;


    @NonNull
    @Column(name = "avatar_url")
    private String  avatarUrl;

    @NonNull
    @JsonIgnore
    @OneToMany(targetEntity = Ledger.class,cascade = CascadeType.ALL,mappedBy = "user")
    @ToString.Exclude
    private List<Ledger> ledgers;

    @NonNull
    @JsonIgnore
    @OneToMany(targetEntity = Account.class,cascade = CascadeType.ALL,mappedBy = "user")
    @ToString.Exclude
    private List<Account> accounts;

    public User(@NonNull Set<Role> roles, @NonNull String nickName, @NonNull String username, @NonNull String password, String avatarUrl) {
        super(roles);
        this.nickName = nickName;
        this.username = username;
        this.password = password;
        this.avatarUrl   = avatarUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
