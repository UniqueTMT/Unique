package com.unique.repository.exam;

import com.unique.dto.exam.CategoryQuizCountDTO;
import com.unique.dto.answer.StudentExamResultDTO;
import com.unique.dto.exam.ExamDetailDTO;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import com.unique.entity.exam.ExamEntity;
import jakarta.persistence.OrderBy;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Long> {


    @EntityGraph(attributePaths = {"quizList"}, type = EntityGraph.EntityGraphType.LOAD)
    @BatchSize(size = 10)
    @OrderBy("regdate ASC")
    List<ExamEntity> findAll();

    // Lazy로딩일 경우 quizList를 Redis에 캐싱할 수 없음 때문에 Eager로딩으로 변경
    @Query("SELECT e FROM ExamEntity e JOIN FETCH e.quizList WHERE e.examSeq = :examSeq")
    Optional<ExamEntity> findWithQuizListByExamSeq(@Param("examSeq") Long examSeq);

    //문제은행 카테고리별 시험지 상세 보기
    @Query("SELECT new com.unique.dto.exam.ExamDetailDTO( " +
            "e.subjectName, " +
            "CAST(e.subjectCode AS string), " +
            "CAST(e.examCnt AS string), " +
            "CAST(e.examSeq AS string), " +
            "q.quiz, q.objYn, q.obj1, q.obj2, q.obj3, q.obj4, " +
            "CAST(q.correctScore AS string), " +
            "q.correctAnswer, q.hint, q.comments) " +
            "FROM ExamEntity e JOIN e.quizList q " +
            "WHERE e.subjectCode = :subjectCode")
    List<ExamDetailDTO> findExamWithQuizListBySubjectCode(@Param("subjectCode") String subjectCode);

    //문제은행 리스트 업
    @Query("SELECT new com.unique.dto.exam.CategoryQuizCountDTO(e.subjectName, e.subjectCode, COUNT(q.quizSeq)) " +
            "FROM ExamEntity e JOIN e.quizList q " +
            "GROUP BY e.subjectName, e.subjectCode")
    List<CategoryQuizCountDTO> findQuizCountGroupedByCategory();

    // 유저가 생성한 시험지 조회
    @Query("SELECT e FROM ExamEntity e WHERE e.member.userSeq = :userSeq")
    List<ExamEntity> findByUserSeq(Long userSeq);


}