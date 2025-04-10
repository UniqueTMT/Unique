package com.unique.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "MEMBER_LOG")
@Getter
@Setter
@ToString(exclude = {"member"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_log_seq_gen")
    @SequenceGenerator(name = "member_log_seq_gen", sequenceName = "MEMBER_LOG_SEQUENCE", allocationSize = 1)
    @Column(name = "MEMBER_LOG_SEQ")
    private Long memberLogSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SEQ")
    @JsonIgnore
    private MemberEntity member;

    @Column(name = "REGDATE", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @Column(name = "LOG_ACTIVE", length = 200)
    private String logActive;

    @Column(name = "USER_IP", length = 50)
    private String userIp;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) {
            this.regdate = new Date();
        }
    }
}
