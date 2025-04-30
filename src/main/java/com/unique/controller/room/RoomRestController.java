package com.unique.controller.room;
import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.room.OpenRoomDTO;
import com.unique.dto.room.RoomDTO;
import com.unique.entity.room.RoomEntity;
import com.unique.impl.room.RoomServiceImpl;
import com.unique.service.answer.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomRestController {
    private final RoomServiceImpl roomService;
    private final AnswerService answerService;

    // 특정 시험방 + 특정 시험지 정보 한번에 조회
//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<RoomEntity>> ctlRoomDetail(@PathVariable(value="id") Long id) {
//        return ResponseEntity.ok(roomService.svcRoomDetail(id));
//    }
//
//    //전체 시험 방 조회
//    @GetMapping("/list")
//    public ResponseEntity<List<RoomDTO>> ctlFindAll() {
//        return ResponseEntity.ok(roomService.findRoomWithExams());
//    }

    // 특정 시험방 + 특정 시험지 정보 한번에 조회
    @GetMapping("/{id}")
    public ResponseEntity<Optional<RoomEntity>> ctlRoomDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(roomService.svcRoomDetail(id));
    }

    //전체 시험 방 조회
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> ctlFindAll() {

        List<OpenRoomDTO> list = roomService.findActiveRooms();

        Map<String, Object> result = new HashMap<>();
        result.put("examList_msm", list);

        return ResponseEntity.ok(result);
    }

    // 시험방 생성
    @PostMapping("/create")
    public ResponseEntity<Long> ctlRoomInsert(@RequestBody RoomDTO roomDTO) {
//        Long userSeq = getCurrentUserSeq(); //로그인 유저 ID 가져오기
        Long roomSeq = roomService.svcRoomInsert(roomDTO, 1L);     // 일단 테스트용으로 1번 유저로 하드코딩
        return ResponseEntity.ok(roomSeq);
    }

    //시험 방 관리 - 정렬
    @GetMapping("/room-array")
    public ResponseEntity<List<RoomEntity>> ctlGetRoomList(
            @RequestParam(defaultValue = "asc") String sort) {
        return ResponseEntity.ok(roomService.svcGetRoomsByOrder(sort));
    }

    // 시험방 남은시간 알림 기능 구현
    @GetMapping("/{roomSeq}/remaining-time")
    public ResponseEntity<Long> getRemainingTime(@PathVariable Long roomSeq) {
        Long remainingTime = roomService.getRemainingTime(roomSeq);
        return ResponseEntity.ok(remainingTime);
    }

    // 응시 답안 제출 -> 저장 -> 채점
    @PostMapping("/submit-answer")
    public ResponseEntity<String> submitAnswer(@RequestBody AnswerDTO answerDTO) {
        answerService.saveOrUpdateAnswer(answerDTO);
        return ResponseEntity.ok("답안 저장 완료");
    }

    @PutMapping("/room")
    public void ctlRoomUpdate(@RequestBody RoomEntity entity) {
        roomService.svcRoomUpdate(entity);
    }

    //시험방 삭제
    @DeleteMapping("/room/{id}")
    public void ctlRoomDelete(@PathVariable(value="id") Long id) {
        roomService.svcRoomDelete(id);
    }

}
