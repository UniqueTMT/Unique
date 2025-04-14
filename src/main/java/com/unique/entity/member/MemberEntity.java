package com.unique.entity.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unique.entity.room.RoomEntity;
import com.unique.entity.applys.ApplysEntity;
import com.unique.entity.exam.ExamEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@ToString(exclude = {"logList", "roomList", "examList", "applysList"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_gen")
    @SequenceGenerator(name = "member_seq_gen", sequenceName = "MEMBER_SEQUENCE", allocationSize = 1)
    @Column(name = "USER_SEQ")
    private Long userSeq;

    @Column(name = "USERID")
    private Long userid;

    @Column(name = "USERPW", length = 100)
    private String userpw;

    @Column(name = "USERNAME", length = 100)
    private String username;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "ROLE", length = 10)
    private String role;

    @Column(name = "NICKNAME", length = 100)
    private String nickname;

    @Column(name = "REGDATE", columnDefinition = "date default sysdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) {
            this.regdate = new Date();
        }
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @BatchSize(size = 10)
    @OrderBy("regdate ASC")
    private List<MemberLogEntity> logList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @BatchSize(size = 10)
    @OrderBy("regdate ASC")
    private List<RoomEntity> roomList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @BatchSize(size = 10)
    @OrderBy("regdate ASC")
    private List<ExamEntity> examList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @BatchSize(size = 10)
    @OrderBy("regdate ASC")
    private List<ApplysEntity> applysList;
}