package com.pplip.global.cache.utils;

import lombok.Getter;

@Getter
public enum CacheType {
    REFRESH("refreshTokens"),
    ATTRACTION("attractions"),
    PLAN("plans")
    ;

    private final String name;

    CacheType(String name) {
        this.name = name;
    }
}
