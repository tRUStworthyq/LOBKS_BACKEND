package com.project.lobks.security;

import com.project.lobks.entity.Role;
import com.project.lobks.entity.StatusUser;
import com.project.lobks.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {


    private Long id;
    private String username;
    private String email;
    private String password;
    private StatusUser statusUser;
    private Role role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return statusUser.equals(StatusUser.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return statusUser.equals(StatusUser.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return statusUser.equals(StatusUser.ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return statusUser.equals(StatusUser.ACTIVE);
    }
}
