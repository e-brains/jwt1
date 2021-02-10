package com.kye.jwt.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않는다.
		.and()
		.formLogin().disable() // 폼 로그인을 안쓴다 
		.httpBasic().disable()  // http 로그인 방식을 안쓴다.
		.authorizeRequests()
		.antMatchers("/api/v1/user/**")
		.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // user, manager, admin 들어감
		.antMatchers("/api/v1/manager/**")
		.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //  manager, admin 들어감
		.antMatchers("/api/v1/admin/**")
		.access("hasRole('ROLE_ADMIN')") // admin 들어감
		.anyRequest().permitAll();  //나머지는 모두 들어감
		
	}
	
	

}
