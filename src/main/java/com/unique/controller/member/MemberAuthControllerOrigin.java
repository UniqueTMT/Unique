package com.unique.controller.member;

// 로그인 요청 객체
import com.unique.dto.member.LoginRequestDTO;

// HttpServlet 관련 객체
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

// 응답을 위한 ResponseEntity
import org.springframework.http.ResponseEntity;

// 인증 관련 예외 및 객체
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * [MemberAuthController]
        * - 설명: 로그인 요청을 처리하는 REST 전용 인증 컨트롤러
 * - 경로: /api/member/login
 * - 주요 기능:
        *   1. 학번(userId)과 비밀번호(userPw)로 로그인 처리
 *   2. 인증 성공 시 세션에 SecurityContext 저장
 *   3. 인증 실패 시 사용자 친화적인 에러 메시지 반환
 */
@RestController                        // JSON 응답을 반환하는 REST 컨트롤러
@RequestMapping("/api/member")      // API 경로 앞부분 공통
@RequiredArgsConstructor               // final 필드에 대한 생성자 자동 생성
public class MemberAuthControllerOrigin {

    // 인증을 담당하는 AuthenticationManager (Custom한 ProviderManager 주입됨)
    private final AuthenticationManager authenticationManager;

    /**
     * [POST /api/member/login]
     * - 로그인 요청 처리 (userId, userPw 포함된 JSON 요청)
     * - 인증 성공 → 세션에 저장 + 성공 메시지 반환
     * - 인증 실패 → 상황별 에러 메시지 반환
     */
    @PostMapping("/loginorign")
    public ResponseEntity<?> ctlLoginProcess(@RequestBody LoginRequestDTO request, HttpServletRequest req) {
        try {
            // 1. 사용자가 입력한 아이디/비밀번호로 인증 객체 생성 후 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserId(), request.getUserPw())
            );

            // 2. 인증 성공 시 SecurityContextHolder에 인증 정보 저장
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            // 3. 인증된 SecurityContext를 세션에도 저장 (세션 기반 인증 유지용)
            HttpSession session = req.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    securityContext
            );

            // 4. 로그인 성공 응답 반환 (로그인 사용자 아이디 포함)
            return ResponseEntity.ok().body(
                    Map.of("message", "로그인 성공", "userId", authentication.getName())
            );

        } catch (UsernameNotFoundException e) {
            // 학번이 존재하지 않을 경우의 예외 처리
            return ResponseEntity.status(401).body(Map.of("error", "존재하지 않는 아이디입니다."));

        } catch (BadCredentialsException e) {
            // 비밀번호가 틀렸을 경우의 예외 처리
            return ResponseEntity.status(401).body(Map.of("error", "비밀번호가 일치하지 않습니다."));

        } catch (AuthenticationException e) {
            // 기타 모든 인증 실패 상황 (계정 비활성화 등)
            return ResponseEntity.status(401).body(Map.of("error", "아이디 또는 비밀번호를 확인하세요."));
        }
    }

}