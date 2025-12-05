package com.pplip.domain.auth.persistence.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements UserDetails {
    private Long id;
    private Long userId;

    private String email;
    private String password;
    private Role role;
    private LocalDateTime passwordUpdatedAt;
    private LocalDateTime createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = () -> getRole().name();
        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}
