
package com.example.todo.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

import com.example.todo.security.JwtAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http 시큐리티 빌더
		http.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors설정
			.and()
			.csrf().disable()		// csfr는 현재 사용하지 않으므로 disable
			.httpBasic().disable()	// token을 사용하므로 basic 인증 disable
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 기반이 아님을 선언
			.and()
			.authorizeRequests().antMatchers("/", "/auth/**").permitAll() 	// /와 /auth/** 경로는 인증 안해도 됨.
			.anyRequest().authenticated();									// /와 /auth/** 이외의 모든 경로는 인증 해야 됨.
		
		// filter 등록
		// 매 요청마다 CorsFilter 실행한 후에 jwtAuthenticationFilter 실행
		http.addFilterBefore(jwtAuthenticationFilter, SecurityContextHolderAwareRequestFilter.class);
	}
}
