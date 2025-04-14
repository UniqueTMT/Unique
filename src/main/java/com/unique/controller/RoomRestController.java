package com.unique.controller;
import com.unique.dto.RoomDTO;
import com.unique.entity.RoomEntity;
import com.unique.service.RoomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RoomRestController {
    private final RoomServiceImpl roomService;


    @GetMapping("/room/{id}")
    public ResponseEntity<Optional<RoomEntity>> ctlRoomDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(roomService.svcRoomDetail(id));
    }

    // 시험방 생성
    @PostMapping("/room")
    public ResponseEntity<Long> ctlRoomInsert(@RequestBody RoomDTO roomDTO) {
//        Long userSeq = getCurrentUserSeq(); // 로그인 유저 ID 가져오기
        Long roomSeq = roomService.svcRoomInsert(roomDTO, 1L);     // 일단 테스트용으로 1번 유저로 하드코딩
        return ResponseEntity.ok(roomSeq);
    }

    // 시험방 남은시간 알림 기능 구현
    @GetMapping("/{roomSeq}/remaining-time")
    public ResponseEntity<Long> getRemainingTime(@PathVariable Long roomSeq) {
        Long remainingTime = roomService.getRemainingTime(roomSeq);
        return ResponseEntity.ok(remainingTime);
    }

    @PutMapping("/room")
    public void ctlRoomUpdate(@RequestBody RoomEntity entity) {
        roomService.svcRoomUpdate(entity);
    }

    @DeleteMapping("/room/{id}")
    public void ctlRoomDelete(@PathVariable(value="id") Long id) {
        roomService.svcRoomDelete(id);
    }

    //시험 방관리
    @GetMapping("/roomlist")
    public ResponseEntity<List<RoomDTO>> ctlFindAll() {
        return ResponseEntity.ok(roomService.findRoomWithExams());
    }
}
