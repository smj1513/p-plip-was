package com.pplip.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.pplip.global.cache.utils.CacheType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.pplip.domain.auth.jwt.JwtProperties.REFRESH_TOKEN_EXPIRE_TIME;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean(name = "caffeineCacheManager")
	@Primary
	public CacheManager cacheManager() {
		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CacheType.REFRESH.getName());
		caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
				.expireAfterWrite(REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS));

		return caffeineCacheManager;
	}

	@Bean(name = "simpleCacheManager")
	public CacheManager simpleCacheManager() {
		SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
		simpleCacheManager.setCaches(List.of(
				new ConcurrentMapCache(CacheType.ATTRACTION.getName()),
				new ConcurrentMapCache(CacheType.PLAN.getName())
		));
		return simpleCacheManager;
	}


	@Bean(name = "refreshTokenCache")
	public Cache refreshTokenCache(@Qualifier("caffeineCacheManager") CacheManager cacheManager) {
		return cacheManager.getCache(CacheType.REFRESH.getName());
	}

	@Bean(name = "attractionCache")
	public Cache attractionCache(@Qualifier("simpleCacheManager") CacheManager cacheManager) {
		return cacheManager.getCache(CacheType.ATTRACTION.getName());
	}

	@Bean(name = "planCache")
	public Cache planCache(@Qualifier("simpleCacheManager") CacheManager cacheManager) {
		return cacheManager.getCache(CacheType.PLAN.getName());
	}


}
