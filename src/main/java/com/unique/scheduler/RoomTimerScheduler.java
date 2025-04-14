package com.unique.scheduler;

import com.unique.dto.room.RoomTimeResponseDTO;
import com.unique.entity.room.RoomEntity;
import com.unique.repository.room.RoomRepository;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomTimerScheduler {

  private final RedisTemplate<String, Object> redisTemplate;
  private final SimpMessagingTemplate messagingTemplate;
  private final RoomRepository roomRepository;

  @Scheduled(fixedRate = 1000)
  public void broadcastRemainingTime() {
    List<RoomEntity> rooms = roomRepository.findAll();

    for (RoomEntity room : rooms) {
      Long roomSeq = room.getRoomSeq();
      String remainingTimeKey = "room:" + roomSeq + ":remainingTime";

      // 방의 시작시간과 제한시간
      Date startTime = room.getStartTime();
      Integer limitTime = room.getLimitTime(); // 분 단위

      // 현재 시간
      long now = System.currentTimeMillis();

      if (startTime == null || limitTime == null) {
        // 시작시간 또는 제한시간이 없는 방은 skip
        continue;
      }

      long startMillis = startTime.getTime();
      long totalMillis = limitTime * 60 * 1000L;
      long endMillis = startMillis + totalMillis;

      RoomTimeResponseDTO response;

      // 진행 전
      if (now < startMillis) {
        response = new RoomTimeResponseDTO(null, "진행전", "시험이 아직 시작되지 않았습니다.");
      }
      // 진행 완료
      else if (now >= endMillis) {
        room.setRoomStatus("진행완료");
        roomRepository.save(room);
        redisTemplate.delete(remainingTimeKey);

        response = new RoomTimeResponseDTO(0L, "진행완료", "시험이 종료되었습니다.");
      }
      // 진행 중
      else {
        long remainingMillis = endMillis - now;
        long remainingSeconds = Math.max(remainingMillis / 1000, 0);

        redisTemplate.opsForValue().set(remainingTimeKey, remainingSeconds);

        response = new RoomTimeResponseDTO(remainingSeconds, "진행중", "시험 진행 중입니다.");
      }

      messagingTemplate.convertAndSend("/topic/room/" + roomSeq, response);
    }
  }
}




