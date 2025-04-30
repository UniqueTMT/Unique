package com.unique.controller.room;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unique.dto.answer.AnswerDTO;
import com.unique.dto.exam.ExamDTO;
import com.unique.dto.room.OpenRoomDTO;
import com.unique.dto.room.RoomDTO;
import com.unique.dto.room.RoomDetailDTO;
import com.unique.dto.room.WrapperRoomDTO;
import com.unique.entity.room.RoomEntity;
import com.unique.impl.room.RoomServiceImpl;
import com.unique.repository.room.RoomRepository;
import com.unique.service.answer.AnswerService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomRestController {
    private final RoomServiceImpl roomService;
    private final AnswerService answerService;
    private final ObjectMapper objectMapper;
    private final RoomRepository roomRepository;

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
    public ResponseEntity<Long> ctlRoomInsert(@RequestBody WrapperRoomDTO wrapperRoomDTO) {
        System.out.println("WrapperRoomDTO 수신 성공!");

        RoomDTO roomDTO = wrapperRoomDTO.getData().getRoomCreateMap();
        ExamDTO examDTO = wrapperRoomDTO.getData().getExam(); // 추가로 exam 꺼내기

        if (examDTO != null) {
            roomDTO.setExam(examDTO); // roomDTO.exam에 직접 세팅
        }

        System.out.println(roomDTO);
        System.out.println(examDTO);

        Long roomSeq = roomService.svcRoomInsert(roomDTO, 1L);

        return ResponseEntity.ok(roomSeq);
    }

    // 제한시간 알림
    @GetMapping("/room/{roomSeq}")
    public Map<String, Object> getRoomDetail(@PathVariable Long roomSeq) {
        RoomDetailDTO roomDetail = roomService.getRoomDetail(roomSeq);

        Map<String, Object> response = new HashMap<>();
        response.put("roomClock", roomDetail); // remainClock 키로 감싸서 보내기

        return response;
    }

    //시험 방 관리 - 정렬
    @GetMapping("/room-array")
    public ResponseEntity<List<RoomEntity>> ctlGetRoomList(
            @RequestParam(defaultValue = "asc") String sort) {
        return ResponseEntity.ok(roomService.svcGetRoomsByOrder(sort));
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
