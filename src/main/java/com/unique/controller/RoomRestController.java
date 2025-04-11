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

    @GetMapping("/room")
    public ResponseEntity<List<RoomEntity>> ctlRoomList()  {
        return ResponseEntity.ok(roomService.svcRoomList());
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<Optional<RoomEntity>> ctlRoomDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(roomService.svcRoomDetail(id));
    }

    @PostMapping("/room")
    public void ctlRoomInsert(@RequestBody RoomEntity entity) {
        roomService.svcRoomInsert(entity);
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
