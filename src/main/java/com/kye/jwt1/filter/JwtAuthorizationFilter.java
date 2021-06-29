package com.kye.jwt1.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kye.jwt1.auth.PrincipalDetails;
import com.kye.jwt1.jwt.JwtProperties;
import com.kye.jwt1.model.User;
import com.kye.jwt1.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 filter를 가지고 있는데 그 필터중에 BasicAuthenticationFilter라는 것이 있음
// 권한이나 인증에 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어 있음
// 만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 안탄다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository){
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 된다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //super.doFilterInternal(request, response, chain); 삭제해야 한다. 안그러면 맨 마지각 chain.doFilter로 2번 타게 된다.
        System.out.println("인증이나 권한이 필요한 주소가 요청됨");

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        System.out.println("jwtHeader : " + jwtHeader);

        // header가 없거나 Bearer로 시작하지 않으면 문제가 있으므로 필터에 넘기고 반환한다.
        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰을 검증을 해서 정상적인 사용자인지 검증
        String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "" );
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build().verify(jwtToken).getClaim("username").asString();

        // JWT서명이 정상적으로 되어 있으면 username이 들어 있을 것이다.
        if (username != null){
            User user = userRepository.findByUsername(username);

            // 일반적인 로그인으로 자동 생성되는 Authentication 객체가 아니라
            // JWT서명이 정상이기 때문에 문제 없다고 판단하고 강제로 Authentication 객체를 만드는 상황이다.
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);  // 다시 필터를 타게 한다.
    }

}
