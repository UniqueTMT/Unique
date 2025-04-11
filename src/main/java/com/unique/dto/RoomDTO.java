package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long roomSeq; //방 시퀀스
    private String roomName; //방 이름
    private Long userSeq; //방 생성 유저명
    private Character viewYn; //방 공개여부
    private String roomPw; //방 비밀번호
    private Integer limitTime; //제한시간
    private Integer limitCnt; //인원제한
    private Character activeYn; //활성여부
    private String roomStatus; //방상태_진행대기완료
    private String shutdownYn; //강제종료 여부
    private Long examSeq; // 시험지 시퀀스
    private Long subjectSeq; // 과목코드
    private String subjectName; // 과목명
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;


}
