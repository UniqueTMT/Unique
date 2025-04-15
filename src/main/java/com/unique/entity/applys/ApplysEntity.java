package com.unique.entity.applys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unique.entity.exam.ExamEntity;
import com.unique.entity.member.MemberEntity;
import com.unique.entity.answer.AnswerEntity;
import com.unique.entity.appeal.AppealEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@ToString(exclude = {"answerList", "appealList"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "APPLYS")
public class ApplysEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPLYS_SEQ")
    @SequenceGenerator(name = "APPLYS_SEQ", sequenceName = "APPLYS_SEQUENCE", allocationSize = 1)
    private Long applysSeq;

    @Column(name = "regdate", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    // 총점
    @Column(name = "total_score")
    private Integer totalScore;

    // 맞힌문제
    @Column(name = "correct_count")
    private Integer correctCount;

    // 틀린문제
    @Column(name = "wrong_count")
    private Integer wrongCount;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) this.regdate = new Date();
    }


    @OneToMany(mappedBy = "applys", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AnswerEntity> answerList;

    @OneToOne(mappedBy = "applys", cascade = CascadeType.ALL, orphanRemoval = true)
    private AppealEntity appeal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    @JsonIgnore
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_seq")
    @JsonIgnore
    private ExamEntity exam;


}