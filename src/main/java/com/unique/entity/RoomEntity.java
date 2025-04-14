package com.unique.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

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

    @Column(name = "room_name", nullable = false, length = 100)
    private String roomName;

    @Column(name = "view_yn", length = 1)
    private String viewYn;

    @Column(name = "room_pw", length = 100)
    private String roomPw;

    @Column(name = "limit_time")
    private Integer limitTime;

    @Column(name = "limit_cnt")
    private Integer limitCnt;

    @Column(name = "active_yn", length = 1)
    private String activeYn;

    @Column(name = "room_status", length = 50)
    private String roomStatus;

    @Column(name = "shutdown_yn", length = 1)
    private String shutdownYn;

    @Column(name = "regdate", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @Column(name = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) this.regdate = new Date();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    @JsonIgnore
    private MemberEntity member;

    @OneToOne
    @JoinColumn(name = "exam_seq")
    private ExamEntity exam;

}
