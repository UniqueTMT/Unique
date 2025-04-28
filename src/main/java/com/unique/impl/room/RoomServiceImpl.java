package com.unique.impl.room;

import com.unique.dto.exam.ExamDTO;
import com.unique.dto.quiz.QuizDTO;
import com.unique.dto.room.OpenRoomDTO;
import com.unique.dto.room.RoomDTO;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.entity.member.MemberEntity;
import com.unique.entity.room.RoomEntity;
import com.unique.repository.exam.ExamRepository;
import com.unique.repository.room.RoomRepository;
import com.unique.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public Optional<RoomEntity> svcRoomDetail(Long id) {
        return roomRepository.findById(id);
    }

    // 시험방 저장 + Redis 정답, 힌트, 배점 저장
    public Long svcRoomInsert(RoomDTO roomDTO, Long userSeq) {
        RoomEntity roomEntity = RoomEntity.builder()
            .roomName(roomDTO.getRoomName())
            .roomPw(roomDTO.getRoomPw())
            .limitTime(roomDTO.getLimitTime())
            .limitCnt(roomDTO.getLimitCnt())
            .activeYn("Y")
            .roomStatus("진행전")       // 진행전, 진행중, 진행완료
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

        // 1. 시험방 저장
        roomRepository.save(roomEntity);

        // 2. Redis 캐싱 - 정답 / 힌트 / 배점 저장
        ExamEntity examEntity = roomEntity.getExam();

        if (examEntity != null && examEntity.getQuizList() != null) {
            List<QuizEntity> quizList = examEntity.getQuizList();

            for (QuizEntity quiz : quizList) {
                String redisKey = "room:" + roomEntity.getRoomSeq() + ":quiz:" + quiz.getQuizSeq();

                Map<String, String> quizMap = new HashMap<>();
                quizMap.put("question", quiz.getQuiz());
                quizMap.put("objYn", quiz.getObjYn());                  // 1: 객관식 2: 주관식 3: 혼합방식 확인용
                quizMap.put("option1", quiz.getObj1());
                quizMap.put("option2", quiz.getObj2());
                quizMap.put("option3", quiz.getObj3());
                quizMap.put("option4", quiz.getObj4());
                quizMap.put("correct", quiz.getCorrectAnswer());
                quizMap.put("hint", quiz.getHint());
                quizMap.put("score", String.valueOf(quiz.getCorrectScore()));   // 배점 저장용

                redisTemplate.opsForHash().putAll(redisKey, quizMap);
            }
        }

        return roomEntity.getRoomSeq();
    }

    public void svcRoomUpdate(RoomEntity entity) {
        roomRepository.save(entity);
    }

    public void svcRoomDelete(Long id) {
        roomRepository.deleteById(id);
    }

    // 열려있는 시험 전체 조회
    public List<OpenRoomDTO> findActiveRooms() {
        return roomRepository.findAllActiveRooms().stream()
            .map(room -> {
                OpenRoomDTO dto = new OpenRoomDTO();

                dto.setRoomSeq(room.getRoomSeq());
                dto.setRoomName(room.getRoomName());
                dto.setStartTime(room.getStartTime());
                dto.setLimitCnt(room.getLimitCnt());
                dto.setLimitTime(room.getLimitTime());
                dto.setHasPassword(
                    (room.getRoomPw() != null && !room.getRoomPw().isEmpty()) ? "Y" : "N");
                dto.setPassword(room.getRoomPw());

                // category, creatorNickname
                if (room.getExam() != null) {
                    dto.setCategory(room.getExam().getSubjectName());

                    if (room.getExam().getMember() != null) {
                        dto.setCreatorNickname(room.getExam().getMember().getNickname());
                    }
                }

                return dto;
            })
            .collect(Collectors.toList());
    }


    //시험 방 관리 - 정렬
    @Override
    public List<RoomEntity> svcGetRoomsByOrder(String sort) {
        if ("desc".equalsIgnoreCase(sort)) {
            return roomRepository.findAllByOrderByRegdateDesc();
        } else {
            return roomRepository.findAllByOrderByRegdateAsc();
        }
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
