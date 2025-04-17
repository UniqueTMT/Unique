package com.unique.impl.admin;

import com.unique.dto.admin.*;
import com.unique.repository.admin.AdminRepository;
import com.unique.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    // 1-1. 카테고리별 전체 시험 수
    @Override
    public List<AdminExamStatsDTO> getExamStatsByCategory() {
        return adminRepository.getExamStatsByCategory();
    }

    // 1-2. 월별 시험 응시 추이
    @Override
    public List<AdminExamMonthlyStatsDTO> getMonthlyApplyStats() {
        return adminRepository.getMonthlyApplyStats();
    }

    // 1-3. 최근 시험 목록
    @Override
    public List<AdminRecentExamDTO> getRecentExams() {
        return adminRepository.getRecentExams();
    }

    // 2-1. 진행 중인 시험방 수
    @Override
    public AdminRoomCountDTO getRunningRoomCount() {
        Long count = adminRepository.getRunningRoomCount();
        return new AdminRoomCountDTO(count);
    }

    // 2-2. 시험방 목록
    @Override
    public List<AdminRoomDTO> getRoomList() {
        return adminRepository.getRoomList();
    }

    // 3. 회원 접속 로그
    @Override
    public List<AdminUserLogDTO> getUserLogs() {
        return adminRepository.getUserLogs();
    }
}
