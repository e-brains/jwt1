package com.kye.jwt1.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("필터1");
        chain.doFilter(request, response); // 주의 : 다시 chain에 넘겨줘야 계속 진행된다.
    }
}
