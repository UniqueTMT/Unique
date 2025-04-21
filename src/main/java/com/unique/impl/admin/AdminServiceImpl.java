package com.unique.impl.admin;

import com.unique.dto.admin.*;
import com.unique.repository.admin.*;
import com.unique.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminExamStatsRepository examStatsRepo;
    private final AdminExamMonthlyStatsRepository examMonthlyRepo;
    private final AdminRecentExamRepository recentExamRepo;
    private final AdminRoomRepository roomRepo;
    private final AdminMemberLogRepository logRepo;

    /**
     * Repository를 통해 과목별 전체 시험 수를 조회하고 반환
     * @return
     */
    @Override
    public List<AdminExamStatsDTO> svcGetExamStatsByCategory() {
        return examStatsRepo.getExamStatsByCategory();
    }

    /**
     * Repository를 통해 월별 응시 추이를 조회하고 반환
     * @return
     */
    @Override
    public List<AdminExamMonthDTO> svcGetMonthlyApplyStats() {
        return examMonthlyRepo.getMonthlyApplyStats();
    }

    /**
     * Repository를 통해 최근 생성된 시험 목록을 조회하고 반환
     * @return
     */
    @Override
    public List<AdminRecentExamDTO> svcGetRecentExams() {
        return recentExamRepo.getRecentExams();
    }

    /**
     * Repository를 통해 진행 중인 시험방 수를 조회하고 DTO로 포장하여 반환
     * @return
     */
    @Override
    public AdminRoomCountDTO svcGetRunningRoomCount() {
        Long count = roomRepo.getRunningRoomCount();
        return new AdminRoomCountDTO(count);
    }

    /**
     * Repository를 통해 전체 시험방 목록을 조회하고 반환
     * @return
     */
    @Override
    public List<AdminRoomDTO> svcGetRoomList() {
        return roomRepo.getRoomList();
    }

    /**
     * Repository를 통해 회원 접속 로그를 최신순으로 조회하고 반환
     * @return
     */
    @Override
    public List<AdminUserLogDTO> svcGetUserLogs() {
        return logRepo.getUserLogs();
    }


}
