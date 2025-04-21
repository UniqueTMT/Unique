package com.unique.controller.admin;

import com.unique.dto.admin.*;
import com.unique.dto.member.MemberInfoDTO;
import com.unique.entity.member.MemberEntity;
import com.unique.impl.member.MemberServiceImpl;
import com.unique.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * [REST] 관리자용 통계 API
 */

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {
    private final MemberServiceImpl memberService;
    private final AdminService adminService;


    @GetMapping("")
    public Map<String, String> testConnection() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return response;
    }


    /**
     * [GET] 카테고리별 전체 시험지 수 통계 (Grafana 연동용)
     * - JSON 응답을 {"data": [...]} 형태로 감싸 반환
     * - Grafana JSON API Plugin에서 인식 가능하도록 구조 변경
     * @return Map<String, Object> - key: "data", value: List<AdminExamStatsDTO>
     */
    @GetMapping("/exam-stats/category")
    @Operation(summary = "카테고리별 전체 시험지 수 조회", description = "과목별 등록된 시험 수를 반환합니다.")
    public Map<String, Object> ctlGetExamStatsByCategory() {
        List<AdminExamStatsDTO> result = adminService.svcGetExamStatsByCategory();

        Map<String, Object> response = new HashMap<>();
        response.put("data", result);

        return response;
    }


    /**
     * [GET] 카테고리별 월별 시험 응시 추이 통계 (Grafana 연동용)
     * @return JSON {"data": List<AdminExamMonthDTO>} 형태로 반환
     */
    @GetMapping("/exam-stats/monthly")
    @Operation(summary = "월별 시험 응시 추이 조회", description = "과목별 월 단위 시험 응시 횟수를 반환합니다.")
    public ResponseEntity<Map<String, List<AdminExamMonthDTO>>> ctlGetMonthlyApplyStats() {
        List<AdminExamMonthDTO> monthly = adminService.svcGetMonthlyApplyStats();
        return ResponseEntity.ok(Map.of("data", monthly));
    }


    /**
     * [GET] 최근 생성된 시험 목록 조회
     * @return List<AdminRecentExamDTO>
     */
    @GetMapping("/exam-stats/recent")
    @Operation(summary = "최근 생성된 시험 목록", description = "최신 등록된 시험지를 조회합니다.")
    public ResponseEntity<List<AdminRecentExamDTO>> ctlGetRecentExams() {
        return ResponseEntity.ok(adminService.svcGetRecentExams());
    }


    /**
     * [GET] 현재 진행 중인 시험방 수 조회
     * @return AdminRoomCountDTO
     */
    @GetMapping("/rooms/active-count")
    @Operation(summary = "진행 중인 시험방 수", description = "현재 진행 중인 시험방의 개수를 조회합니다.")
    public ResponseEntity<AdminRoomCountDTO> ctlGetRunningRoomCount() {
        return ResponseEntity.ok(adminService.svcGetRunningRoomCount());
    }


    /**
     * [GET] 시험방 목록 조회
     * @return List<AdminRoomDTO>
     */
    @GetMapping("/rooms")
    @Operation(summary = "시험방 목록 조회", description = "등록된 전체 시험방 목록을 조회합니다.")
    public ResponseEntity<List<AdminRoomDTO>> ctlGetRoomList() {
        return ResponseEntity.ok(adminService.svcGetRoomList());
    }


    /**
     * [GET] 회원 접속 로그 조회
     * @return List<AdminUserLogDTO>
     */
    @GetMapping("/logs/user")
    @Operation(summary = "회원 접속 로그 조회", description = "회원의 접속 활동 로그를 조회합니다.")
    public ResponseEntity<List<AdminUserLogDTO>> ctlGetUserLogs() {
        return ResponseEntity.ok(adminService.svcGetUserLogs());
    }


//----------------------------------------------------------------------------


    //관리자 기능 관련 컨트롤러 입니다. 추후 사용 예정입니다..

    @GetMapping("/admin")
    public ResponseEntity<List<MemberEntity>> ctlMemberList() {
        return ResponseEntity.ok(memberService.svcMemberList());
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Optional<MemberEntity>> ctlMemberDetail(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.svcMemberDetail(id));
    }

    @PostMapping("/admin")
    public void ctlMemberInsert(@RequestBody MemberEntity entity) {
        memberService.svcMemberInsert(entity);
    }

    @PutMapping("/admin")
    public void ctlMemberUpdate(@RequestBody MemberEntity entity) {
        memberService.svcMemberUpdate(entity);
    }

    @DeleteMapping("/admin/{id}")
    public void ctlMemberDelete(@PathVariable(value="id") Long id) {
        memberService.svcMemberDelete(id);
    }

    @GetMapping("/admin-info/{userSeq}")
    public ResponseEntity<Optional<MemberInfoDTO>> ctlMemberInfo(@PathVariable(value = "userSeq") Long userSeq) {
        return ResponseEntity.ok(memberService.svcGetMemberInfo(userSeq));
    }

    //--------------------------------------------------------------------------------------------
//    // 1-1. 카테고리별 전체 시험 수
//    @GetMapping("/statistics/exam-count")
//    @Operation(summary = "카테고리별 전체 시험 수 조회", description = "과목별 등록된 시험 수를 반환합니다.")
//    public List<AdminExamStatsDTO> getExamStats() {
//        return adminService.getExamStatsByCategory();
//    }

//    // 1-2. 월별 시험 응시 추이
//    @GetMapping("/statistics/exam-monthly")
//    @Operation(summary = "월별 시험 응시 추이", description = "과목별 월 단위 시험 응시 횟수를 조회합니다.")
//    public List<AdminExamMonthlyStatsDTO> getMonthlyStats() {
//        return adminService.getMonthlyApplyStats();
//    }
//
//    // 1-3. 최근 시험 목록
//    @GetMapping("/statistics/recent-exams")
//    @Operation(summary = "최근 생성된 시험 목록", description = "최신 등록된 시험지를 조회합니다.")
//    public List<AdminRecentExamDTO> getRecentExams() {
//        return adminService.getRecentExams();
//    }
//
//    // 2-1. 진행 중인 시험방 수
//    @GetMapping("/rooms/active-count")
//    @Operation(summary = "진행 중인 시험방 수", description = "현재 진행 중인 시험방의 개수를 조회합니다.")
//    public AdminRoomCountDTO getRunningRoomCount() {
//        return adminService.getRunningRoomCount();
//    }
//
//    // 2-2. 시험방 목록
//    @GetMapping("/rooms")
//    @Operation(summary = "시험방 목록", description = "등록된 전체 시험방 목록을 조회합니다.")
//    public List<AdminRoomDTO> getRoomList() {
//        return adminService.getRoomList();
//    }
//
//    // 3. 회원 접속 로그
//    @GetMapping("/logs/user-log")
//    @Operation(summary = "회원 접속 로그", description = "회원의 접속 활동 로그를 조회합니다.")
//    public List<AdminUserLogDTO> getUserLogs() {
//        return adminService.getUserLogs();
//    }
}