package com.unique.repository.appeal;

import com.unique.entity.appeal.AppealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppealRepository extends JpaRepository<AppealEntity, Long> {

        /*
        * function : 교수 이의제기 리스트
        * author : 차경준
        * regdate : 25.04.15
        * */
        @Query("SELECT a FROM AppealEntity a " +
                "JOIN FETCH a.applys ap " +
                "JOIN FETCH ap.member m " +
                "JOIN FETCH ap.exam e " +
                "JOIN FETCH e.member em " +
                "WHERE em.userSeq = :userSeq")
        List<AppealEntity> myFindAppealList(@Param("userSeq") Long userSeq);


}