package com.unique.service;
import com.unique.dto.RoomDTO;
import com.unique.entity.ExamEntity;
import com.unique.entity.MemberEntity;
import com.unique.entity.RoomEntity;
import com.unique.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public List<RoomEntity> svcRoomList() {
        return roomRepository.findAll();
    }

    public Optional<RoomEntity> svcRoomDetail(Long id) {
        return roomRepository.findById(id);
    }

    public Long svcRoomInsert(RoomDTO roomDTO, Long userSeq) {
        RoomEntity roomEntity = RoomEntity.builder()
            .roomName(roomDTO.getRoomName())
            .roomPw(roomDTO.getRoomPw())
            .limitTime(roomDTO.getLimitTime())
            .limitCnt(roomDTO.getLimitCnt())
            .activeYn("Y")
            .roomStatus("ACTIVE")
            .shutdownYn("N")
            .viewYn("Y")
            .regdate(roomDTO.getRegdate())
            .build();

        // 출제자 (유저) 매핑
        MemberEntity memberEntity = MemberEntity.builder()
            .userSeq(userSeq)
            .build();

        roomEntity.setMember(memberEntity);

        // 시험지 연관
        if (roomDTO.getExam() != null && roomDTO.getExam().getExamSeq() != null) {
            ExamEntity examEntity = ExamEntity.builder()
                .examSeq(roomDTO.getExam().getExamSeq())
                .build();
            roomEntity.setExam(examEntity);
        }

        roomRepository.save(roomEntity);

        return roomEntity.getRoomSeq();
    }


    public void svcRoomUpdate(RoomEntity entity) {
        roomRepository.save(entity);
    }

    public void svcRoomDelete(Long id) {
        roomRepository.deleteById(id);
    }

    //시험 방 관리
    public List<RoomDTO> findRoomWithExams(){
        return roomRepository.findRoomWithExam().stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());
    }

    // 시험방 남은시간 알림
    public Long getRemainingTime(Long roomSeq) {
        String remainingTimeKey = "room:" + roomSeq + ":remainingTime";
        Object redisValue = redisTemplate.opsForValue().get(remainingTimeKey);

        if (redisValue != null) {
            return Long.parseLong(redisValue.toString());
        }

        // Redis 없으면 DB fallback
        RoomEntity room = roomRepository.findById(roomSeq)
            .orElseThrow(() -> new RuntimeException("시험방을 찾을 수 없습니다."));

        if (room.getStartTime() == null || room.getLimitTime() == null) {
            throw new RuntimeException("시험이 아직 시작되지 않았습니다.");
        }

        long elapsedMillis = System.currentTimeMillis() - room.getStartTime().getTime();
        long totalMillis = room.getLimitTime() * 60 * 1000L;
        long remainingMillis = totalMillis - elapsedMillis;

        return Math.max(remainingMillis / 1000, 0); // 초 단위 반환
    }
}
