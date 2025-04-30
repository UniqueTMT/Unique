package com.unique.entity.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unique.entity.member.MemberEntity;
import com.unique.entity.quiz.QuizEntity;
import com.unique.entity.room.RoomEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"quizList"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "EXAM")
public class ExamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXAM_SEQ")
    @SequenceGenerator(name = "EXAM_SEQ", sequenceName = "EXAM_SEQUENCE", allocationSize = 1)
    private Long examSeq;

    @Column(name = "subject_code")
    private Long subjectCode;

    @Column(name = "subject_name", length = 20)
    private String subjectName;

    @Column(name = "exam_title", length = 200)
    private String examTitle;

    @Column(name = "exam_cnt")
    private Integer examCnt;

    @Column(name = "pub_yn", length = 1)
    private String pubYn;

    @Column(name = "regdate", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) this.regdate = new Date();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    @JsonIgnore
    private MemberEntity member;

    //문제은행 카테고리별 문제 상세 보기
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @BatchSize(size = 10)
    @OrderBy("regdate ASC")
    private List<QuizEntity> quizList;
    
    //응시자 답안 상세
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_seq") // ★ EXAM 테이블의 room_seq 외래키 기준
    @JsonIgnore
    private RoomEntity room;


}
