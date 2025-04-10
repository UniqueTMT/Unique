package com.unique.repository;

import com.unique.entity.AnswerEntity;
import com.unique.entity.AppealEntity;
import com.unique.entity.ApplysEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ApplysRepository extends JpaRepository<ApplysEntity, Long> {
}