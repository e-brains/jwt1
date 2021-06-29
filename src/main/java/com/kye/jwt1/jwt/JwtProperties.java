package com.kye.jwt1.jwt;

public interface JwtProperties {
    String SECRET = "kye"; // 우리 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 1800000; // 30분 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";  // Bearer에서 한칸 띄울 것
    String HEADER_STRING = "Authorization";
}
