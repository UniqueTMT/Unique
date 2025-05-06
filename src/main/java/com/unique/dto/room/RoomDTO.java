package com.unique.dto.room;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unique.dto.exam.ExamDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long roomSeq;          // 시험방 번호
    private String roomName;       // 시험방 이름
    private String category;       // 시험방 카테고리 (시험지 subjectName)
    private String hasPassword;    // 비밀번호 유무 (Y/N)
    private String roomPw;         // 비밀번호
    private Integer limitTime;     // 제한 시간
    private Integer limitCnt;      // 인원 제한
    private String roomStatus;     // 방 상태 (진행전, 진행중, 진행완료)
    private String viewYn;         // 공개방/비공개방 유무
    private Long userSeq;
    private Boolean confirmed;     // 2차 채점 완료 여부 추가

    private ExamDTO exam;          // 선택된 시험지

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;        // 시작 시간

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;
  
    //시험 방관리
    private List<ExamDTO> examList;
}