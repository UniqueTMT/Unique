package com.unique.controller.room;
import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.room.RoomDTO;
import com.unique.entity.room.RoomEntity;
import com.unique.impl.room.RoomServiceImpl;
import com.unique.service.answer.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomRestController {
    private final RoomServiceImpl roomService;
    private final AnswerService answerService;


    @GetMapping("/room/{id}")
    public ResponseEntity<Optional<RoomEntity>> ctlRoomDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(roomService.svcRoomDetail(id));
    }

    // 시험방 생성
    @PostMapping("/room")
    public ResponseEntity<Long> ctlRoomInsert(@RequestBody RoomDTO roomDTO) {
//        Long userSeq = getCurrentUserSeq(); //로그인 유저 ID 가져오기
        Long roomSeq = roomService.svcRoomInsert(roomDTO, 1L);     // 일단 테스트용으로 1번 유저로 하드코딩
        return ResponseEntity.ok(roomSeq);
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
