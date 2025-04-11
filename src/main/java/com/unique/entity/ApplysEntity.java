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