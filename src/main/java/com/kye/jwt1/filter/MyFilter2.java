package com.kye.jwt1.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter2 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("필터2");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8"); // 한글로 받기 위해서

        // 내가 제공한 토큰(kye) 일때 만 진입하고 아니면 접속을 막는다.
        if (req.getMethod().equals("POST")) { // 대문자이며 POST 요청일 때만 동작
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth ==== " + headerAuth);

            // 토큰 제공시점 : 처음 접속 시 id/pw로 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어서 응답을 해준다.
            // 그 후 요청할 때 마다 header의 Authorization에 value값(영문)으로 토큰을 가져온다.
            // 그때 내가 제공한 토큰인지 검증만 하면된다. (RSA, HS256으로 만들어진 토큰)
            if (headerAuth.equals("token")){
                chain.doFilter(req, res);  // 주의 : 다시 chain에 넘겨줘야 계속 진행된다.
            }else {
                PrintWriter printWriter = res.getWriter();
                printWriter.println("인증 안됨"); // 다시 chain에 넘겨주지 않으면 여기서 멈춤.
            }
        }

    }
}
