package com.unique.controller;
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
    private final RoomServiceImpl roomServiceImpl;

    @GetMapping("/room")
    public ResponseEntity<List<RoomEntity>> ctlRoomList()  {
        return ResponseEntity.ok(roomServiceImpl.svcRoomList());
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<Optional<RoomEntity>> ctlRoomDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(roomServiceImpl.svcRoomDetail(id));
    }

    @PostMapping("/room")
    public void ctlRoomInsert(@RequestBody RoomEntity entity) {
        roomServiceImpl.svcRoomInsert(entity);
    }

    @PutMapping("/room")
    public void ctlRoomUpdate(@RequestBody RoomEntity entity) {
        roomServiceImpl.svcRoomUpdate(entity);
    }

    @DeleteMapping("/room/{id}")
    public void ctlRoomDelete(@PathVariable(value="id") Long id) {
        roomServiceImpl.svcRoomDelete(id);
    }

}
