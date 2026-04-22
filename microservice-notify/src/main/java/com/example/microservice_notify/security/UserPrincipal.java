package com.example.microservice_notify.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class UserPrincipal implements UserDetails {

    private final Integer userId;
    private final String email;
    private final String passwordHash;
    private final String role;
    private final boolean enabled;

    public UserPrincipal(Integer userId, String email, String passwordHash, String role, boolean enabled) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.enabled = enabled;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String normalizedRole = role == null
                ? "CITIZEN"
                : role.trim().replace('-', '_').replace(' ', '_').toUpperCase(Locale.ROOT);
        return List.of(new SimpleGrantedAuthority("ROLE_" + normalizedRole));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }
}

