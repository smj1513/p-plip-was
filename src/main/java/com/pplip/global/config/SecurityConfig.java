package com.pplip.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplip.domain.auth.filter.CustomLoginFilter;
import com.pplip.domain.auth.filter.JwtAuthenticationFilter;
import com.pplip.domain.auth.filter.JwtExceptionFilter;
import com.pplip.domain.auth.filter.handler.CustomLoginFailureHandler;
import com.pplip.domain.auth.filter.handler.CustomLoginSuccessHandler;
import com.pplip.domain.auth.jwt.JwtUtil;
import com.pplip.domain.auth.persistence.entity.Role;
import com.pplip.domain.auth.provider.JwtAuthenticationProvider;
import com.pplip.global.cache.usecase.RefreshTokenCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	private final JwtUtil jwtUtil;
	private final JwtAuthenticationProvider provider;
	private final ObjectMapper om;
	private final AuthenticationConfiguration authenticationConfiguration;
	private final RefreshTokenCacheService refreshTokenCacheService;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtAuthenticationProvider provider) {
		return new JwtAuthenticationFilter(provider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(HttpBasicConfigurer::disable);
		http.formLogin(FormLoginConfigurer::disable);
		http.csrf(CsrfConfigurer::disable);

		http.cors((cors) -> cors.configurationSource(corsConfigurationSource()));

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// api 설계 필요.
		http.authorizeHttpRequests(req ->
				req
						.requestMatchers(
								contextPath + "/v3/api-docs/**",
								contextPath + "/swagger-ui/**",
								contextPath + "/swagger-ui.html",
								contextPath + "/swagger-resources/**", // 추가 필요
								contextPath + "/webjars/**"             // 추가 필요
						).permitAll()
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/user/join/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/freeboard").permitAll()
						.requestMatchers(HttpMethod.GET, "/freeboard/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/notice").permitAll()
						.requestMatchers(HttpMethod.GET, "/notice/**").permitAll()
						.requestMatchers("/notice/**").hasAuthority("ADMIN")
						.requestMatchers("/error-code").permitAll()
						.requestMatchers(HttpMethod.GET, "/trip/attraction/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/trip/plan/{planId}/todo").permitAll()
						.anyRequest().authenticated());
		http.addFilterAt(customLoginFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(new JwtAuthenticationFilter(provider), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(new JwtExceptionFilter(om), JwtAuthenticationFilter.class);
		return http.build();
	}

	public AbstractAuthenticationProcessingFilter customLoginFilter() throws Exception {
		CustomLoginFilter clf = new CustomLoginFilter(om);
		clf.setFilterProcessesUrl("/auth/login");
		clf.setAuthenticationManager(authenticationManager(authenticationConfiguration));
		clf.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler(jwtUtil, om, refreshTokenCacheService));
		clf.setAuthenticationFailureHandler(new CustomLoginFailureHandler(om));
		return clf;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// 1. 프론트엔드 주소 (정확히 일치해야 함)
		configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000", "https://m.pplip.c01.kr"));

		// 2. ★ 핵심: PATCH 메서드가 반드시 포함되어야 함 ★
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

		// 3. 모든 헤더 허용
		configuration.setAllowedHeaders(List.of("*"));

		// 4. 쿠키/인증정보 포함 허용 (프론트에서 withCredentials: true를 쓰고 있으므로 필수)
		configuration.setAllowCredentials(true);

		// 5. 브라우저가 Authorization 헤더를 읽을 수 있게 허용 (JWT 쓸 때 중요)
		configuration.setExposedHeaders(List.of("Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
