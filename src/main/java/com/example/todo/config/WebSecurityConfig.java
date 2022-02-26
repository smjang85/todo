
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
		// http ��ť��Ƽ ����
		http.cors() // WebMvcConfig���� �̹� ���������Ƿ� �⺻ cors����
			.and()
			.csrf().disable()		// csfr�� ���� ������� �����Ƿ� disable
			.httpBasic().disable()	// token�� ����ϹǷ� basic ���� disable
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session ����� �ƴ��� ����
			.and()
			.authorizeRequests().antMatchers("/", "/auth/**").permitAll() 	// /�� /auth/** ��δ� ���� ���ص� ��.
			.anyRequest().authenticated();									// /�� /auth/** �̿��� ��� ��δ� ���� �ؾ� ��.
		
		// filter ���
		// �� ��û���� CorsFilter ������ �Ŀ� jwtAuthenticationFilter ����
		http.addFilterBefore(jwtAuthenticationFilter, SecurityContextHolderAwareRequestFilter.class);
	}
}
