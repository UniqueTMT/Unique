package com.unique.repository;
import com.unique.entity.MemberLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLogRepository extends JpaRepository<MemberLogEntity, Long> {
}
