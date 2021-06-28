package com.kye.jwt1.config;

import com.kye.jwt1.filter.JwtAuthenticationFilter;
import com.kye.jwt1.filter.MyFilter1;
import com.kye.jwt1.filter.MyFilter2;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new MyFilter2(), SecurityContextPersistenceFilter.class); // 내가 만든 필터가 제일먼저 실행되도록 한다.
        http.csrf().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다. (STATELESS 서버로 만들겠다는 의미)
                .and()
                .addFilter(corsFilter)  // 내가 CorsFilterConfig에 정의한 corsfilter()메서드를 실행한다.
                .formLogin().disable()  // JWT를 쓰기 때문에 폼 로그인을 안쓴다.
                .httpBasic().disable()  // JWT를 쓰기 때문에 기본적인 http 방식의 로그인을 안쓴다.
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))  // AuthenticationManager를 파라미터로 넘겨야 하는데 이 객체는 WebSecurityConfigurerAdapter 가 가지고 있다.
                .authorizeRequests()
                .antMatchers("/api/v1/user/**") // 권한이 user, manager, admin 인 사람 접근 가능
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**") // 권한이 manager, admin 인 사람 접근 가능
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**") // 권한이 admin 인 사람 접근 가능
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll(); // 다른 uri에 대해서는 모두 접근 가능하게 한다.

    }
}
