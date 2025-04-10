package com.unique.service;
import com.unique.entity.RoomEntity;
import com.unique.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    public List<RoomEntity> svcRoomList() {
        return roomRepository.findAll();
    }

    public Optional<RoomEntity> svcRoomDetail(Long id) {
        return roomRepository.findById(id);
    }

    public void svcRoomInsert(RoomEntity entity) {
        roomRepository.save(entity);
    }

    public void svcRoomUpdate(RoomEntity entity) {
        roomRepository.save(entity);
    }

    public void svcRoomDelete(Long id) {
        roomRepository.deleteById(id);
    }
}
