package com.pplip.global.cache.usecase;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.pplip.global.cache.utils.CacheType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

import static com.pplip.domain.auth.jwt.JwtProperties.REFRESH_TOKEN_EXPIRE_TIME;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("RefreshTokenCacheService 통합 테스트")
class RefreshTokenCacheServiceTest {

    @Autowired
    private RefreshTokenCacheService refreshTokenCacheService;

    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager cacheManager;

    private Cache cache;

    @AfterEach
    void tearDown() {
        // 테스트 후 캐시 클리어
        cacheManager.getCache(CacheType.REFRESH.getName()).clear();
    }


    @Test
    @DisplayName("리프레시 토큰 저장 및 조회")
    void saveAndGetRefreshToken() {
        // given
        String refreshToken = "testRefreshToken";
        Long userId = 1L;

        // when
        refreshTokenCacheService.saveRefreshToken(refreshToken, userId);

        // then
        Long foundUserId = refreshTokenCacheService.getRefreshTokenById(refreshToken);
        assertThat(foundUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("리프레시 토큰 삭제")
    void removeRefreshToken() {
        // given
        String refreshToken = "testRefreshToken";
        Long userId = 1L;
        refreshTokenCacheService.saveRefreshToken(refreshToken, userId);

        // when
        refreshTokenCacheService.removeRefreshToken(refreshToken);

        // then
        Long foundUserId = refreshTokenCacheService.getRefreshTokenById(refreshToken);
        assertThat(foundUserId).isNull();
    }

    @Test
    @DisplayName("리프레시 토큰 유효성 검사")
    void validateRefreshToken() {
        // given
        String validToken = "validToken";
        String invalidToken = "invalidToken";
        Long userId = 1L;
        refreshTokenCacheService.saveRefreshToken(validToken, userId);

        // when
        boolean isValid = refreshTokenCacheService.validateRefreshToken(validToken);
        boolean isInvalid = refreshTokenCacheService.validateRefreshToken(invalidToken);

        // then
        assertThat(isValid).isTrue();
        assertThat(isInvalid).isFalse();
    }

    @Test
    public void multiValue() throws Exception {
        // given
        for (int i = 0; i < 5; i++) {
            String validToken = "validToken" + i;
            Long userId = (long) i;
            refreshTokenCacheService.saveRefreshToken(validToken, userId);
        }

        for (int i = 0; i < 5; i++) {
            String validToken = "validToken" + i;
            boolean isValid = refreshTokenCacheService.validateRefreshToken(validToken);
            assertThat(isValid).isTrue();
        }
    }


}
