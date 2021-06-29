package com.kye.jwt1.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kye.jwt1.auth.PrincipalDetails;
import com.kye.jwt1.jwt.JwtProperties;
import com.kye.jwt1.model.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 기존에 있음
// '/login'요청해서 username, password를 전송하면 (post방식)
// UsernamePasswordAuthenticationFilter가 동작을 함
@RequiredArgsConstructor // AuthenticationManager 필드에 값을 넣어주는 생성자가 자동으로 만들어 진다.
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // '/login' 주소로 요청이 오면 로그인 시도를 위해서 실행되는 메서드이다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : attemptAuthentication() 실행 : 로그인 시도 중......");

//        BufferedReader br = request.getReader(); // 버퍼에 읽어 들인다.
//        String input = null;
//        while ((input = br.readLine()) != null) { // 한 라인씩 읽으면서 파싱
//            System.out.println(input);
//        }
        /*****************************************************************************************
         1. username, password를 받는다
         2. 토큰을 만들어서 정상인지 로그인을 시도 해 본다.
         authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출되고
         loadByUsername()메서드가 실행된다.

         3. loadByUsername()에서 리턴된 PrincipalDetails를 권한관리를 위해서 세션에 담는다.
         세션에 담지 않으면 권한관리에 문제가 생긴다.
         만약 권한관리가 필요없다면 세션에 담을 필요가 없다.

         4. JWT 토큰을 만들어서 응답하면 된다.
        ******************************************************************************************/
        try {

            // 1. username, password를 받는다
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);

            System.out.println("user.getUsername == " + user.getUsername());

            // 2. 토큰을 만들어서 정상인지 로그인을 시도 해 본다.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 3. 토큰을 파라미터로 넘겨서 authenticationManager를 실행시킨다.
            // 그러면 PrincipalDetailsService의 loadUserByUsername()이 실행된 후 정상이면 authentication에 할당 됨
            // DB에 있는 username과 password가 일치한다는 의미 => 인증 완료
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 4. authentication에는 로그인 정보가 담겨 있고 로그인이 되었다는 의미
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("principalDetails.getUser().getUsername() ===> " + principalDetails.getUser().getUsername());

            // authentication이 리턴될 때 session영역에 저장된다.
            // 리턴의 이유는 권한관리를 security가 대신 해주기 때문에 편하려고 하는 거임
            // 굳이 JWT토큰을 사용하면서 세션을 만들 이유가 없음. (권한처리 때문에 세션에 넣어 주는 것임)

            return authentication;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    // attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication메서드가 실행된다.
    // 그래서 여기서 JWT토큰을 만들어서 request에 대해 JWT 토큰을 response하면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증이 정상적으로 완료됨 ");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // JWT라이브러리를 이용하여 만든다. (RSA방식은 아니고 Hash암호 방식, 특징은 씨크릿 값이 있어야 함)
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())  // 토큰 이름
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME)) // 토큰 만료시간 30분
                .withClaim("id", principalDetails.getUser().getId())  // 비공개 클레임이고 넣고 싶은 데이터를 넣으면 된다.
                .withClaim("username", principalDetails.getUser().getUsername())  // 비공개 클레임이고 넣고 싶은 데이터를 넣으면 된다.
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));  // 서버만 알고 있는 씨크릿 코드 값

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
        
    }
}
