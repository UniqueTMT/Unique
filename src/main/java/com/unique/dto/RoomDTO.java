package com.unique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String viewYn; //방 공개여부
    private String roomPw; //방 비밀번호
    private Date limitTime; //제한시간
    private Long limitCnt; //인원제한


//            ACTIVE_YN
//    REGDATE
//            ROOM_STATUS
//    SHUTDOWN_YN

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;


}
