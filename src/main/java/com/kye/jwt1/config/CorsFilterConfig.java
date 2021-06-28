package com.kye.jwt1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig {

    // corsFilter를 빈으로 등록해 놓고 SecurityConfig에서 사용한다.
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // 내 서버가 응답을 할때 json을 자바스크립트에서 받을 수 있게 할지를 설정하는 것
        config.addAllowedOrigin("*");  // 모든 ip에 응답을 허용하겠다.
        config.addAllowedHeader("*");  // 모든 header에 응답을 허용하겠다.
        config.addAllowedMethod("*");  // 모든 post, get, put, delete, patch 요청을 허용하겠다.
        source.registerCorsConfiguration("/api/**", config); // api 이하 모든 접근은 config설정을 따르라는 의미
        return new CorsFilter(source);
    }
}
