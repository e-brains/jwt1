package com.kye.jwt1.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 기존에 있음
// '/login'요청해서 username, password를 전송하면 (post방식)
// UsernamePasswordAuthenticationFilter가 동작을 함
@RequiredArgsConstructor // AuthenticationManager 필드에 값을 넣어주는 생성자가 자동으로 만들어 진다.
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // '/login' 주소로 요청이 오면 로그인 시도를 위해서 실행되는 메서드이다.
    @Override
    public Authentication  attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : attemptAuthentication() 실행 : 로그인 시도 중......");

        // 1. username, password를 받는다

        // 2. 정상인지 로그인을 시도 해 본다.
        // authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출되고
        // loadByUsername()메서드가 실행된다.


        // 3. loadByUsername()에서 리턴된 PrincipalDetails를 권한관리를 위해서 세션에 담는다.
        // 세션에 담지 않으면 권한관리에 문제가 생긴다.
        // 만약 권한관리가 필요없다면 세션에 담을 필요가 없다.


        // 4. JWT 토큰을 만들어서 응답하면 된다.


        return super.attemptAuthentication(request, response);
    }
}
