package com.unique.entity.appeal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unique.entity.applys.ApplysEntity;
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
@Table(name = "APPEAL")
public class AppealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPEAL_SEQ")
    @SequenceGenerator(name = "APPEAL_SEQ", sequenceName = "APPEAL_SEQUENCE", allocationSize = 1)
    private Long appealSeq;

    @Column(name = "contents", length = 1000)
    private String contents;

    @Column(name = "appeal_title", length = 500)
    private String appealTitle;

    @Column(name = "regdate", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) this.regdate = new Date();
    }

    @OneToOne
    @JoinColumn(name = "applys_seq")
    private ApplysEntity applys;

}
