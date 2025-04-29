package com.unique.controller.member;

import java.util.Map;

// 응답을 위한 ResponseEntity
import org.springframework.http.ResponseEntity;
// 인증 관련 예외 및 객체
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 로그인 요청 객체
import com.unique.dto.member.LoginRequestDTO;
import com.unique.dto.member.LoginRequestWrapper;

// HttpServlet 관련 객체
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * [MemberAuthController]
 * 래퍼 DTO 방식으로 클라이언트 JSON 요청을 처리하는 예시
 */
@RestController                        // JSON 응답을 반환하는 REST 컨트롤러
@RequestMapping("/api/member")      // API 경로 앞부분 공통
@RequiredArgsConstructor               // final 필드에 대한 생성자 자동 생성
public class MemberAuthController {

    // 인증을 담당하는 AuthenticationManager (Custom한 ProviderManager 주입됨)
    private final AuthenticationManager authenticationManager;

    
    /**
     * [POST /api/member/login]
     * @param wrapper 	: 클라이언트가 보낸 최상위 JSON을 매핑한 래퍼 DTO
     * @param req		: HttpServletRequest (세션 저장용)
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> ctlLoginProcess(@RequestBody LoginRequestWrapper wrapper, HttpServletRequest req) {

    	// 1) 래퍼에서 실제 로그인 정보 꺼내기
        LoginRequestDTO loginDto = wrapper.getData().getDmLogin();
    	System.out.println("loginDto.getUserId() : " + loginDto.getUserId());
    	System.out.println("loginDto.getUserPw() : " + loginDto.getUserPw());
    	
        try {
        	// 2) Spring Security 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDto.getUserId(),
                    loginDto.getUserPw()
                )
            );

            // 3) 인증 성공 시 SecurityContext 및 세션에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            req.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
            );
            
            // payload 준비
            Map<String, String> dmLoginPayload = Map.of(
                "userId", authentication.getName(),
                "message", "로그인 성공"
            );
            // 응답 전체를 한번에 담을 Map
            Map<String, Object> responseBody = Map.of("dmLogin", dmLoginPayload);

            // ★여기서 콘솔에 출력
            System.out.println("[MemberAuthController] responseBody = " + responseBody);

            // 최종 반환
            return ResponseEntity.ok(responseBody);

        } catch (UsernameNotFoundException ex) {
            Map<String, String> dmLoginPayload = Map.of(
            		"message", "아이디, 패스워드를 확인하세요."
    		);
            Map<String, Object> responseBody = Map.of("dmLogin", dmLoginPayload);
            System.out.println("[LoginController] 로그인 실패(아이디) 응답 → " + responseBody);
            return ResponseEntity.status(401).body(responseBody);

        } catch (BadCredentialsException ex) {
            Map<String, String> dmLoginPayload = Map.of(
            		"message", "패스워드, 아이디를 확인하세요."
    		);
            Map<String, Object> responseBody = Map.of("dmLogin", dmLoginPayload);
            System.out.println("[LoginController] 로그인 실패(비밀번호) 응답 → " + responseBody);
            return ResponseEntity.status(401).body(responseBody);

        } catch (AuthenticationException ex) {
            Map<String, String> dmLoginPayload = Map.of("message", "아이디 또는 비밀번호를 확인하세요.");
            Map<String, Object> responseBody = Map.of("dmLogin", dmLoginPayload);
            System.out.println("[LoginController] 로그인 실패(기타) 응답 → " + responseBody);
            return ResponseEntity.status(401).body(responseBody);
        }
    }
}