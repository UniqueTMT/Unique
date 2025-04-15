package com.unique.service.appeal;

import com.unique.dto.appeal.AppealDTO;
import com.unique.dto.appeal.AppealDetailDTO;
import com.unique.dto.appeal.AppealPostDTO;
import com.unique.entity.appeal.AppealEntity;

import java.util.List;
import java.util.Optional;

public interface AppealService {

    List<AppealDTO> svcAppealList();

    AppealDetailDTO svcAppealDetail(Long id);
    void svcAppealInsert(AppealEntity entity);
    void svcAppealUpdate(AppealEntity entity);
    void svcAppealDelete(Long id);
    //이의제기 생성 - 경준
    AppealDTO svcAppealCreate(AppealPostDTO appealDTO);
}