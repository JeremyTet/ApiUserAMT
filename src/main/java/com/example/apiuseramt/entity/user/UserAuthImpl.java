package com.example.apiuseramt.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Objects;
public class UserAuthImpl implements UserDetails  {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private String name;

    public Collection<? extends GrantedAuthority> authorities;

    public UserAuthImpl(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static UserAuthImpl build(User admin) {
        return new UserAuthImpl(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPassword()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserAuthImpl admin = (UserAuthImpl) o;
        return Objects.equals(id, admin.id);
    }
}

