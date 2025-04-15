package com.unique.impl.appeal;

import com.unique.dto.appeal.AppealPostDTO;
import com.unique.dto.appeal.AppealDTO;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.member.MemberEntity;
import com.unique.repository.appeal.SseEmitterRepository;
import com.unique.repository.applys.ApplysRepository;
import com.unique.service.appeal.AppealService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.unique.entity.appeal.AppealEntity;
import com.unique.repository.appeal.AppealRepository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {
    private final AppealRepository appealRepository;
    private final ApplysRepository applysRepository;
    private final SseEmitterRepository sseEmitterRepository;

    /*
     * fucntion : 이의제기 이력 리스트 확인
     * author : 차경준
     * regdate : 2025.04.15
     * */
    @Override
    public List<AppealDTO> svcAppealList() {
        Long hardcodedUserSeq = 3L;
        List<AppealEntity> appeals = appealRepository.myFindAppealList(hardcodedUserSeq);
        return appeals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AppealDTO convertToDTO(AppealEntity appeal) {
        ApplysEntity applys = appeal.getApplys();
        ExamEntity exam = applys.getExam();
        MemberEntity professor = exam.getMember();
        MemberEntity student = applys.getMember();

        return AppealDTO.builder()
                .appealSeq(appeal.getAppealSeq())
                .contents(appeal.getContents())
                .appealTitle(appeal.getAppealTitle())
                .applysSeq(applys.getApplysSeq())
                .examSeq(exam.getExamSeq())
                .subjectSeq(exam.getSubjectCode())
                .subjectName(exam.getSubjectName())
                .examTitle(exam.getExamTitle())
                .userSeq(applys.getMember().getUserSeq())      // 학생 userSeq
                .username(applys.getMember().getUsername())    // 학생 이름
                .creatorSeq(applys.getExam().getMember().getUserSeq())  // 교수 userSeq
                .creatorName(applys.getExam().getMember().getUsername()) // 교수 이름
                .regdate(appeal.getRegdate())
                .build();
    }


    /*
    * function : 이의제기 세부 정보
    * author : 차경준
    * regdate : 25.04.15
    *  */
    @Override
    public Optional<AppealEntity> svcAppealDetail(Long id) {
        return appealRepository.findById(id);
    }

    public void svcAppealInsert(AppealEntity entity) {
        appealRepository.save(entity);
    }

    public void svcAppealUpdate(AppealEntity entity) {
        appealRepository.save(entity);
    }

//    public void svcAppealDelete(Long id) {
//        appealRepository.deleteById(id);
//    }

    // 이의제기 삭제 - 경준
    @Override
    @Transactional
    public void svcAppealDelete(Long id) {
        // 1. 삭제할 이의제기 조회
        AppealEntity appeal = appealRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이의제기 ID: " + id));

        // 2. 연관된 ApplysEntity의 참조 제거
        ApplysEntity applys = appeal.getApplys();
        if (applys != null) {
            applys.setAppeal(null); // ApplysEntity에서 AppealEntity 참조 제거
            applysRepository.save(applys); // 변경 사항 저장
        }

        // 3. 이의제기 삭제
        appealRepository.delete(appeal);
    }


    //이의제기 생성 - 경준
    @Override
    @Transactional
    public AppealDTO svcAppealCreate(AppealPostDTO appealDTO) {
        // 1. 응시 기록 조회
        ApplysEntity applys = applysRepository.findById(appealDTO.getApplysSeq())
                .orElseThrow(() -> new RuntimeException("응시 기록 없음"));

        // 2. 출제자(교수) 정보 추출
        MemberEntity professor = applys.getExam().getMember();
        MemberEntity student = applys.getMember();

        // 3. 이의제기 생성 및 저장
        AppealEntity appeal = AppealEntity.builder()
                .contents(appealDTO.getContents())
                .appealTitle(appealDTO.getAppealTitle())
                .applys(applys)
                .build();
        AppealEntity saved = appealRepository.save(appeal);

        // 4. DTO 변환 (학생, 시험, 교수 정보 포함)
        AppealDTO notifyDTO = AppealDTO.builder()
                .appealSeq(saved.getAppealSeq())
                .contents(saved.getContents())
                .appealTitle(saved.getAppealTitle())
                .applysSeq(applys.getApplysSeq())
                .examSeq(applys.getExam().getExamSeq())
                .subjectSeq(applys.getExam().getSubjectCode())
                .subjectName(applys.getExam().getSubjectName())
                .examTitle(applys.getExam().getExamTitle())
                .userSeq(student.getUserSeq())         // 학생 userSeq
                .username(student.getUsername())       // 학생 이름
                .creatorSeq(professor.getUserSeq())    // 교수 userSeq
                .creatorName(professor.getUsername())  // 교수 이름
                .regdate(saved.getRegdate())
                .build();

        // 5. SSE로 알림 전송
        SseEmitter emitter = sseEmitterRepository.getEmitter(professor.getUserSeq());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("appeal-notification")
                        .data(notifyDTO, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                sseEmitterRepository.removeEmitter(professor.getUserSeq());
            }
        }

        return notifyDTO;
    }

    
    // 교수 구독
    public SseEmitter subscribe(Long userSeq) {
        SseEmitter emitter = new SseEmitter(60_000L);
        sseEmitterRepository.addEmitter(userSeq, emitter);

        emitter.onCompletion(() -> sseEmitterRepository.removeEmitter(userSeq));
        emitter.onTimeout(() -> sseEmitterRepository.removeEmitter(userSeq));

        return emitter;
    }








}