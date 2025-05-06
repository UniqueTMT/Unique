package com.unique.impl.room;

import com.unique.dto.exam.ExamDTO;
import com.unique.dto.quiz.QuizDTO;
import com.unique.dto.room.MyRoomStatusDTO;
import com.unique.dto.room.OpenRoomDTO;
import com.unique.dto.room.RoomDTO;
import com.unique.dto.room.RoomDetailDTO;
import com.unique.entity.answer.AnswerEntity;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.entity.member.MemberEntity;
import com.unique.entity.room.RoomEntity;
import com.unique.repository.answer.AnswerRepository;
import com.unique.repository.exam.ExamRepository;
import com.unique.repository.room.RoomRepository;
import com.unique.service.room.RoomService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private final ExamRepository examRepository;
    private final ModelMapper modelMapper;
    private final AnswerRepository answerRepository;

    public Optional<RoomEntity> svcRoomDetail(Long id) {
        return roomRepository.findById(id);
    }

    // 시험방 저장 + Redis 정답, 힌트, 배점 저장
    public Long svcRoomInsert(RoomDTO roomDTO, Long userSeq) {
        // 1. RoomEntity 생성
        RoomEntity roomEntity = RoomEntity.builder()
            .roomName(roomDTO.getRoomName())
            .roomPw(roomDTO.getRoomPw())
            .limitTime(roomDTO.getLimitTime())
            .limitCnt(roomDTO.getLimitCnt())
            .activeYn("Y")
            .roomStatus("진행전") // 진행전, 진행중, 진행완료
            .shutdownYn("N")
            .startTime(roomDTO.getStartTime())
            .viewYn(roomDTO.getViewYn())
            .regdate(roomDTO.getRegdate())
            .build();

        // 2. Member 연관 설정
        MemberEntity memberEntity = MemberEntity.builder()
            .userSeq(userSeq)
            .build();
        roomEntity.setMember(memberEntity);

        // 3. 시험지 연결
        if (roomDTO.getExam() == null) {
            roomDTO.setExam(new ExamDTO());
        }

        Long examSeq = roomDTO.getExam().getExamSeq();
        if (examSeq != null) {
            ExamEntity examEntity = examRepository.findWithQuizListByExamSeq(examSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 시험지를 찾을 수 없습니다."));
            roomEntity.setExam(examEntity);
        }

        // 4. 시험방 저장
        roomRepository.save(roomEntity);

        // 5. Redis 캐싱 - 정답 / 힌트 / 배점 저장
        ExamEntity examEntity = roomEntity.getExam();
        System.out.println("퀴즈 수: " + (examEntity.getQuizList() == null ? "null" : examEntity.getQuizList().size()));

        if (examEntity != null && examEntity.getQuizList() != null) {
            List<QuizEntity> quizList = examEntity.getQuizList();

            for (QuizEntity quiz : quizList) {
                String redisKey = "room:" + roomEntity.getRoomSeq() + ":quiz:" + quiz.getQuizSeq();

                Map<String, String> quizMap = new HashMap<>();
                quizMap.put("question", quiz.getQuiz());
                quizMap.put("objYn", quiz.getObjYn());                  // 1: 객관식 2: 주관식 3: 혼합
                quizMap.put("option1", quiz.getObj1());
                quizMap.put("option2", quiz.getObj2());
                quizMap.put("option3", quiz.getObj3());
                quizMap.put("option4", quiz.getObj4());
                quizMap.put("correct", quiz.getCorrectAnswer());
                quizMap.put("hint", quiz.getHint());
                quizMap.put("score", String.valueOf(quiz.getCorrectScore()));

                // Redis 저장
                redisTemplate.opsForHash().putAll(redisKey, quizMap);

                // TTL 설정: 방 시작시간 + 제한시간 + buffer - 현재시간
                Date startTime = roomEntity.getStartTime();
                Integer limitMinutes = roomEntity.getLimitTime();
                long nowMillis = System.currentTimeMillis();
                long endTimeMillis = startTime.getTime() + (limitMinutes * 60 * 1000L);
                long bufferMillis = 5 * 60 * 1000L; // 5분 buffer
                long ttlMillis = endTimeMillis - nowMillis + bufferMillis;

                if (ttlMillis > 0) {
                    redisTemplate.expire(redisKey, Duration.ofMillis(ttlMillis));
                    System.out.println("TTL 설정 완료: " + (ttlMillis / 1000) + "초");
                } else {
                    System.out.println("TTL 미설정: 시작 시간이 이미 지났거나 이상함");
                }
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

    // 제한시간 알림
    @Override
    public RoomDetailDTO getRoomDetail(Long roomSeq) {
        RoomEntity room = roomRepository.findById(roomSeq)
            .orElseThrow(() -> new RuntimeException("방을 찾을 수 없습니다."));

        RoomDetailDTO dto = new RoomDetailDTO();
        dto.setRoomName(room.getRoomName());
        dto.setStartTime(room.getStartTime());
        dto.setLimitTime(room.getLimitTime());

        // expiredAt 계산
        dto.setExpiredAt(calculateExpiredAt(room.getStartTime(), room.getLimitTime()));

        return dto;
    }

    // 시작시간 + 제한시간(분) 더해서 만료시간 계산
    private Date calculateExpiredAt(Date startTime, Integer limitTimeMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, limitTimeMinutes);
        return calendar.getTime();
    }

    // 시험 방 관리 - 전체 방 조회
    public List<RoomDTO> findRoomWithExams() {
        return roomRepository.findRoomWithExam().stream()
            .map(room -> modelMapper.map(room, RoomDTO.class))
            .collect(Collectors.toList());
    }

    // 시험 방 관리 - 상세 채점 페이지에서 응시자 리스트 및 답안 표시
    public List<AnswerEntity> getAnswersByRoomSeq(Long roomSeq) {
        return answerRepository.findAllByRoomSeq(roomSeq);
    }

    // 유저가 만든 방 조회
    @Override
    public List<MyRoomStatusDTO> getMyRoomStatus(Long userSeq) {
        return roomRepository.findRoomStatusByUser(userSeq);
    }





}
