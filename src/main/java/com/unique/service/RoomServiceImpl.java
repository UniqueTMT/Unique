package com.unique.service;
import com.unique.dto.RoomDTO;
import com.unique.entity.RoomEntity;
import com.unique.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

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

    //시험 방 관리
//    public List<RoomDTO> findRoomWithExams(){
//        return roomRepository.findRoomWithExams().stream()
//                .map(room -> modelMapper.map(room, RoomDTO.class))
//                .collect(Collectors.toList());
//    }

}
