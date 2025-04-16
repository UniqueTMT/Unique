package com.unique.entity.answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.quiz.QuizEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ANSWER")
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANSWER_SEQ")
    @SequenceGenerator(name = "ANSWER_SEQ", sequenceName = "ANSWER_SEQUENCE", allocationSize = 1)
    private Long answerSeq;

    @Column(name = "user_answer", length = 500)
    private String userAnswer;

    @Column(name = "answer_yn", length = 1)
    private String answerYn;

    @Column(name = "regdate", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    // GPT 1차 채점 결과
    @Column(name = "ai_score")
    private Integer aiScore;

    @Column(name = "ai_feedback", length = 1000)
    private String aiFeedback;

    // 교수 2차 채점 결과
    @Column(name = "professor_score")
    private Integer professorScore;

    @Column(name = "professor_feedback", length = 1000)
    private String professorFeedback;

    @Column(name = "confirmed")
    private Boolean confirmed; // true면 확정됨

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) this.regdate = new Date();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applys_seq")
    @JsonIgnore
    private ApplysEntity applys;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_seq")
    @JsonIgnore
    private QuizEntity quiz;
}
