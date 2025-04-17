package com.unique.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * [CustomLogoutSuccessHandler]
 * - 설명 : 로그아웃 성공 시 JSON 형식의 메시지를 반환하는 핸들러
 * - 사용 방식 : Security 설정에서 .logoutSuccessHandler(...)로 등록
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.write("{\"message\": \"로그아웃되었습니다.\"}");
        out.flush();
    }
}
