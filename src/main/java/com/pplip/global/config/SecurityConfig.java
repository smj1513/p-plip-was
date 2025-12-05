package com.pplip.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplip.domain.auth.filter.CustomLoginFilter;
import com.pplip.domain.auth.filter.JwtAuthenticationFilter;
import com.pplip.domain.auth.filter.JwtExceptionFilter;
import com.pplip.domain.auth.filter.handler.CustomLoginFailureHandler;
import com.pplip.domain.auth.filter.handler.CustomLoginSuccessHandler;
import com.pplip.domain.auth.provider.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationProvider provider;
	private final ObjectMapper om;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtAuthenticationProvider provider) {
		return new JwtAuthenticationFilter(provider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(HttpBasicConfigurer::disable);
		http.formLogin(FormLoginConfigurer::disable);
		http.csrf(CsrfConfigurer::disable);
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// api 설계 필요.
		http.authorizeHttpRequests(req ->
				req
						.requestMatchers(
								"/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html"
						).permitAll().anyRequest()
						.permitAll());
		http.addFilterAt(customLoginFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(
				new JwtAuthenticationFilter(provider), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(new JwtExceptionFilter(om), JwtAuthenticationFilter.class);
		return http.build();
	}

	public AbstractAuthenticationProcessingFilter customLoginFilter() {
		CustomLoginFilter clf = new CustomLoginFilter(om);
		clf.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler());
		clf.setAuthenticationFailureHandler(new CustomLoginFailureHandler(om));
		return clf;
	}


}
