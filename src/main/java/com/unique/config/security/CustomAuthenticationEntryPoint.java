package com.unique.config.security;

import jakarta.servlet.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * [CustomAuthenticationEntryPoint]
 * - 인증되지 않은 사용자가 보호된 리소스에 접근할 때 작동
 * - 예: 로그인 안 한 사용자가 /api/member/route 요청할 경우
 * - 응답: 401 + JSON 메시지
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.write("{\"error\": \"로그인 후 이용 가능합니다.\"}");
        out.flush();
    }
}
