package com.unique.impl.member;

import com.unique.dto.member.MemberInfoDTO;
import com.unique.entity.member.MemberEntity;
import com.unique.repository.member.MemberRepository;
import com.unique.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;            // 이메일 전송을 위한 JavaMailSender 빈 (이메일 설정 필수)
    private final PasswordEncoder passwordEncoder;      // 비밀번호 암호화를 위한 PasswordEncoder 빈 (예: BCryptPasswordEncoder)

    /**
     * 사용자 이름과 이메일을 기반으로 userId(학번)를 조회하고 마스킹 처리하여 반환
     * <p>
     * 예시: userId가 "20140293"일 경우 → "20****93"으로 반환
     * </p>
     *
     * @param username 사용자 이름
     * @param email 사용자 이메일
     * @return 마스킹된 userId 문자열, 일치하는 사용자가 없으면 null
     */
    @Override
    public String svcFindUserIdByInfo(String username, String email) {
        Optional<MemberEntity> memberEntity = memberRepository.findByUsernameAndEmail(username, email);

        if (memberEntity.isEmpty()) return null;

        Long userId = memberEntity.get().getUserid();   // DB에서 조회한 사용자 학번
        String idStr = String.valueOf(userId);          // 마스킹 처리를 위한 타입변환

        if (idStr.length() >= 6) {
            return idStr.substring(0, 2) + "****" + idStr.substring(idStr.length() - 2);
        } else {
            return "****"; // 너무 짧은 ID인 경우 최소한의 보호 처리
        }
    }

    /**
     * 사용자 학번, 이름 및 이메일을 기반으로 회원 정보를 확인 후,
     * 무작위 임시 비밀번호를 생성하여 회원의 이메일로 전송한 후,
     * DB의 해당 회원 비밀번호를 암호화된 임시 비밀번호로 업데이트 한다.
     * 사용시기 : 비밀번호 찾기 -> 사용자 입력한 정보가 DB의 값과 동일한 경우
     * @param userId
     * @param username
     * @param email
     * @return boolean 비밀번호 재설정 및 이메일 발송 성공 여부
     */
    @Override
    public boolean svcFindAndResetPassword(Long userId, String username, String email) {

        // 회원 정보 조회
        Optional<MemberEntity> memberEntity = memberRepository.findByUseridAndUsernameAndEmail(userId, username, email);
        if (memberEntity.isEmpty()) {
            throw new RuntimeException("입력하신 정보와 일치하는 회원님의 정보가 없습니다.");        // 해당 정보와 일치하는 회원이 없을 경우 예외 또는 false 반환
        }

        MemberEntity member = memberEntity.get();

        // 무작위 임시 비밀번호 생성 (예시: UUID를 활용하여 앞 8자리 사용)
        String randomPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);

        // 이메일 전송
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[Unique] 임시 비밀번호 안내");
            message.setText("안녕하세요. " + member.getUsername() + "님\n\n"
                    + "요청하신 임시 비밀번호는 다음과 같습니다.\n\n"
                    + randomPassword + "\n\n"
                    + "로그인 후 반드시 비밀번호를 변경해주세요.");
            mailSender.send(message);
        } catch (Exception e) {
            // 이메일 전송 실패시 DB 변경을 롤백하거나 별도 처리가 필요함 (예시에서는 예외 발생)
            e.printStackTrace();    // 콘솔에러 출력
            throw new RuntimeException("이메일 전송에 실패하였습니다.", e);
        }

        // 비밀번호를 암호화하여 DB 업데이트 (Spring Security의 PasswordEncoder 사용)
        String encodePw = passwordEncoder.encode(randomPassword);
        member.setUserpw(encodePw);
        memberRepository.save(member);

        return true;
    }



    public List<MemberEntity> svcMemberList() {
        return memberRepository.findAll();
    }

    public Optional<MemberEntity> svcMemberDetail(Long id) {
        return memberRepository.findById(id);
    }

    public void svcMemberInsert(MemberEntity entity) {
        memberRepository.save(entity);
    }
    public void svcMemberUpdate(MemberEntity entity) {
        memberRepository.save(entity);
    }
    public void svcMemberDelete(Long id) {
        memberRepository.deleteById(id);
    }
    
    // 유저 정보 출력 - 경준
    @Override
    public Optional<MemberInfoDTO> svcGetMemberInfo(Long userSeq) {
        return memberRepository.myfindUserInfo(userSeq);
    }
    
    // 유저 비밀번호 변경 - 경준
    @Override
    public boolean svcChangePassword(Long userSeq, Long userid, String oldPassword, String newPassword) {
            // 유저 조회
            MemberEntity user = memberRepository.findByUserSeq(userSeq);

            if (user == null) {
                throw new RuntimeException("유저를 찾을 수 없습니다.");
            }
            // 유저 아이디 검증 (userid 먼저!)
            if (!user.getUserid().equals(userid)) {
                throw new RuntimeException("유저 아이디가 일치하지 않습니다.");
            }
            // 기존 비밀번호 검증
            if (!user.getUserpw().equals(oldPassword)) {
                throw new RuntimeException("기존 비밀번호가 일치하지 않습니다.");
            }

            // 새 비밀번호 설정 및 저장
            user.setUserpw(newPassword);
            memberRepository.save(user);

            return true;
    }
}
