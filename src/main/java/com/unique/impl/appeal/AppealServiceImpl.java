package com.unique.impl.appeal;

import com.unique.dto.appeal.AppealPostDTO;
import com.unique.entity.applys.ApplysEntity;
import com.unique.repository.applys.ApplysRepository;
import com.unique.service.appeal.AppealService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.unique.entity.appeal.AppealEntity;
import com.unique.repository.appeal.AppealRepository;

@Service
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {
    private final AppealRepository appealRepository;
    private final ApplysRepository applysRepository;

    public List<AppealEntity> svcAppealList() {
        return appealRepository.findAll();
    }

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
    public void svcAppealCreate(AppealPostDTO appealDTO) {
        // 1. 응시 기록 조회
        ApplysEntity applys = applysRepository.findById(appealDTO.getApplysSeq())
                .orElseThrow(() -> new RuntimeException("응시 기록을 찾을 수 없습니다."));

        // 2. 이의제기 엔티티 생성
        AppealEntity appeal = AppealEntity.builder()
                .contents(appealDTO.getContents())
                .appealTitle(appealDTO.getAppealTitle())
                .applys(applys)          // ApplysEntity 연결
                .build();

        // 3. 저장 (regdate는 @PrePersist로 자동 생성)
        appealRepository.save(appeal);
    }
}