package com.unique.repository.admin;

import com.unique.dto.admin.AdminUserLogDTO;
import com.unique.entity.member.MemberLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AdminMemberLogRepository extends JpaRepository<MemberLogEntity, Long> {

    /**
     * [3] 회원 접속 로그
     */
    @Query("SELECT new com.unique.dto.admin.AdminUserLogDTO(m.userid, l.logActive, l.regdate, l.userIp) " +
            "FROM MemberLogEntity l " +
            "JOIN l.member m " +
            "ORDER BY l.regdate DESC")
    List<AdminUserLogDTO> getUserLogs();
}
