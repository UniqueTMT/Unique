package com.unique.controller.exam;

import com.unique.dto.exam.ExamParticipationDTO;
import com.unique.dto.room.OpenRoomDTO;
import com.unique.service.exam.ExamParticipationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam-participation")
@RequiredArgsConstructor
public class ExamParticipationController {

  private final ExamParticipationService examParticipationService;

  // 시험응시 : 시험방 리스트 클릭 -> 입장
  @GetMapping("/{roomSeq}")
  public ResponseEntity<Map<String, Object>> getExamParticipationDetail(
      @PathVariable Long roomSeq,
      @RequestParam Long userSeq // 프론트에서 전달
  ) {
    ExamParticipationDTO dto = examParticipationService.getExamParticipationDetail(roomSeq, userSeq);
    Map<String, Object> result = new HashMap<>();
    result.put("entry_room", dto);
    return ResponseEntity.ok(result);
  }

}
