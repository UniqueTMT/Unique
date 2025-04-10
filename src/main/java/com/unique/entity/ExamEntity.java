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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_seq")
    @JsonIgnore
    private RoomEntity room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    @JsonIgnore
    private MemberEntity member;

    @Column(name = "exam_title", length = 200)
    private String examTitle;

    @Column(name = "exam_cnt")
    private Integer examCnt;

    @Column(name = "pub_yn", length = 1)
    private Character pubYn;

    @Column(name = "regdate", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) this.regdate = new Date();
    }

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @BatchSize(size = 10)
    @OrderBy("regdate ASC")
    private List<QuizEntity> quizList;

}
