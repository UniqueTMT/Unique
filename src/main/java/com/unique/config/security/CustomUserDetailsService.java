package com.unique.config.security;

import com.unique.entity.member.MemberEntity;
import com.unique.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * [CustomUserDetailsService]
 * - 설명 : Spring Security에서 사용자 인증 시 DB에서 사용자 정보를 조회하기 위한 클래스
 * - 구현 인터페이스 : UserDetailsService
 * - 작동 시점 : 로그인 시 Spring Security가 자동으로 loadUserByUsername() 호출
 * - 기능 : 비활성화 계정 체크 로직 추가
 */
@Service                    // Spring Bean 등록 (Component Scan 대상)
@RequiredArgsConstructor    // final 필드 자동 주입용 생성자 생성 (lombok)
public class CustomUserDetailsService implements UserDetailsService {

    // 사용자 정보를 조회하기 위한 Repository
    private final MemberRepository memberRepository;

    /**
     * [loadUserByUsername]
     * - 설명 : Spring Security가 사용자 인증 시 호출하는 메서드
     * - 입력값 : userId (학번)
     * - 출력값 : UserDetails (Spring Security 내부 인증 객체)
     */
    @Override
    public UserDetails loadUserByUsername(String userIdStr) throws UsernameNotFoundException {

        // userId는 학번으로, DB에 Long 타입으로 저장되어 있음 → String → Long 변환
        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            // 숫자가 아닌 형식으로 들어올 경우 예외 처리
            throw new UsernameNotFoundException("잘못된 학번 형식입니다.");
        }

        /// DB에서 userId(학번)으로 사용자 조회 (이전 stream → findByUserid() 변경)
        MemberEntity member = memberRepository.findByUserid(userId)
                .orElseThrow(() -> new UsernameNotFoundException("학번에 해당하는 사용자를 찾을 수 없습니다."));

        // 현재 DB에 isActive 컬럼 사라짐
//        // (선택) 비활성화 계정이면 로그인 차단
//        if (!member.isActive()) { // isActive()는 boolean 필드 getter라 가정
//            throw new DisabledException("비활성화된 계정입니다. 관리자에게 문의하세요.");
//        }

        // CustomUserDetails 객체 생성하여 반환 (Spring Security가 사용할 사용자 정보)
        return new CustomUserDetails(member);
    }
}
