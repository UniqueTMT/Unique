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
    private Character answerYn;

    @Column(name = "regdate", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

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
