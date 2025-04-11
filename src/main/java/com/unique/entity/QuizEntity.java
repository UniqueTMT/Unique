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
@ToString(exclude = {"answerList"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "QUIZ")
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUIZ_SEQ")
    @SequenceGenerator(name = "QUIZ_SEQ", sequenceName = "QUIZ_SEQUENCE", allocationSize = 1)
    private Long quizSeq;


    @Column(name = "quiz", length = 500)
    private String quiz;

    @Column(name = "obj_yn", length = 1)
    private String objYn;

    @Column(name = "obj1", length = 200)
    private String obj1;

    @Column(name = "obj2", length = 200)
    private String obj2;

    @Column(name = "obj3", length = 200)
    private String obj3;

    @Column(name = "obj4", length = 200)
    private String obj4;

    @Column(name = "correct_score")
    private Integer correctScore;

    @Column(name = "correct_answer", length = 200)
    private String correctAnswer;

    @Column(name = "hint", length = 500)
    private String hint;

    @Column(name = "comments", length = 500)
    private String comments;

    @Column(name = "regdate", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) this.regdate = new Date();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_seq")
    @JsonIgnore
    private ExamEntity exam;


    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AnswerEntity> answerList;
}