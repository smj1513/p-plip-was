package com.pplip.domain.auth.persistence.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

public enum Role implements GrantedAuthority {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private String name;


    Role(String name) {
        this.name = name;
    }

    public static Role getRole(String roleName) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name().equals(roleName))
                .findFirst().orElseGet(null);
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
