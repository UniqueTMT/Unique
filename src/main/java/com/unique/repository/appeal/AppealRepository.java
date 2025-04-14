package com.unique.repository.appeal;

import com.unique.entity.appeal.AppealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppealRepository extends JpaRepository<AppealEntity, Long> {
}