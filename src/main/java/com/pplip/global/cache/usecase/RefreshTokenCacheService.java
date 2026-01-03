package com.pplip.global.cache.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenCacheService {

    private final Cache refreshTokenCache;

	public RefreshTokenCacheService(@Qualifier("refreshTokenCache") Cache refreshTokenCache) {
		this.refreshTokenCache = refreshTokenCache;
	}

	public void saveRefreshToken(String refreshToken, Long userId) {
        refreshTokenCache.put(refreshToken, userId);
    }

    public Long getRefreshTokenById(String refreshToken) {
        return refreshTokenCache.get(refreshToken, Long.class);
    }

    public void removeRefreshToken(String refreshToken) {
        refreshTokenCache.evict(refreshToken);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return refreshTokenCache.get(refreshToken) != null;
    }
}
