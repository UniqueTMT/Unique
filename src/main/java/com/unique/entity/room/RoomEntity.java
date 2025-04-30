package com.unique.entity.room;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.member.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString(exclude = {"examList"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ROOM")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROOM_SEQ")
    @SequenceGenerator(name = "ROOM_SEQ", sequenceName = "ROOM_SEQUENCE", allocationSize = 1)
    private Long roomSeq;

    // 방 제목
    @Column(name = "room_name", nullable = false, length = 100)
    private String roomName;

    // 방 공개여부 - 비공개/공개
    @Column(name = "view_yn", length = 1)
    private String viewYn;

    // 방 비밀번호
    @Column(name = "room_pw", length = 100)
    private String roomPw;

    // 제한시간
    @Column(name = "limit_time")
    private Integer limitTime;

    // 제한인원
    @Column(name = "limit_cnt")
    private Integer limitCnt;

    // 시험 활성 여부 - 관리자가 제어(방 강제종료 기능용)
    @Column(name = "active_yn", length = 1)
    private String activeYn;

    // 방 상태 - 진행전, 진행중, 진행종료
    @Column(name = "room_status", length = 50)
    private String roomStatus;

    // 이전에 강제종료가 되었는지 여부
    @Column(name = "shutdown_yn", length = 1)
    private String shutdownYn;

    // 방 생성일
    @Column(name = "regdate", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    // 방 시작시간
    @Column(name = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) this.regdate = new Date();
    }

    // 방을 생성한 유저 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    @JsonIgnore
    private MemberEntity member;

    // 방에서 사용된 시험지
    @OneToOne
    @JoinColumn(name = "exam_seq")
    private ExamEntity exam;

}
