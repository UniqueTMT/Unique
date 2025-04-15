package com.unique.controller.quiz;
import com.unique.dto.quiz.QuizDTO;
import com.unique.entity.quiz.QuizEntity;
import com.unique.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizRestController {
    private final QuizService quizService;

    @PostMapping("/quizlist")
    public void ctlQuizInsert(@RequestBody QuizEntity entity) {
        quizService.svcQuizInsert(entity);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Optional<QuizEntity>> ctlQuizDetail(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(quizService.svcQuizDetail(id));
    }

    //특정 시험(examSeq)에 해당하는 예상문제 목록
    @GetMapping("/list/{examSeq}")
    public List<QuizDTO> ctlGetQuizList(@PathVariable Long examSeq) {
        return quizService.svcGetQuizList(examSeq);
    }

    //특정 문제(quizSeq)에 대해 사용자가 내용을 수정한 결과를 저장
    @PutMapping("/{quizSeq}")
    public void ctlUpdateQuiz(@PathVariable Long quizSeq, @RequestBody QuizDTO dto) {
        quizService.svcUpdateQuiz(quizSeq, dto);
    }

    //특정 문제(quizSeq)를 삭제
    @DeleteMapping("/{quizSeq}")
    public void ctlDeleteQuiz(@PathVariable Long quizSeq) {
        quizService.svcDeleteQuiz(quizSeq);
    }

    //특정 시험(examSeq)에 등록된 문제들을 출제 완료 상태로 전환
    @PostMapping("/publish/{examSeq}")
    public void ctlPublishExam(@PathVariable Long examSeq) {
        quizService.svcPublishExam(examSeq);
    }

//    @GetMapping("/generate")
//    public ResponseEntity<List<QuizDTO>> generateFakeQuizList() {
//        List<QuizDTO> quizList = new ArrayList<>();
//
//        quizList.add(QuizDTO.builder()
//                .quiz("자바에서 변수를 선언하는 올바른 방법은?")
//                .objYn('Y')
//                .obj1("int a = 10;")
//                .obj2("int = 10 a;")
//                .obj3("10 = int a;")
//                .obj4("int: a = 10")
//                .correctAnswer("A")
//                .hint("자바는 자료형 → 변수명 → 값 순으로 선언")
//                .comments("변수 선언 시 자료형과 변수명 순서를 지켜야 한다.")
//                .build()
//        );
//
//        quizList.add(QuizDTO.builder()
//                .quiz("다음 중 자바에서 변수의 특징으로 옳은 것은?")
//                .objYn('Y')
//                .obj1("변수는 한 번 선언하면 값을 바꿀 수 없다.")
//                .obj2("변수는 항상 public 이어야 한다.")
//                .obj3("변수는 데이터를 저장하는 메모리 공간이다.")
//                .obj4("변수는 메서드 내부에서는 선언할 수 없다.")
//                .correctAnswer("C")
//                .hint("변수는 데이터를 저장하기 위해 사용됨")
//                .comments("변수는 메모리에 데이터를 담기 위한 이름이다.")
//                .build()
//        );
//
//        return ResponseEntity.ok(quizList);
//    }

    //출제 완료
//    @PostMapping("/publish")
//    public ResponseEntity<String> publishQuizToExam(@RequestParam Long examSeq) {
//        ExamEntity exam = examRepository.findById(examSeq)
//                .orElseThrow(() -> new RuntimeException("시험을 찾을 수 없습니다."));
//
//        List<QuizEntity> quizzes = quizRepository.findAllByExamIsNull(); // 또는 상태 필터링
//
//        for (QuizEntity quiz : quizzes) {
//            quiz.setExam(exam); // 🔗 문제 ↔ 시험 연결
//        }
//
//        quizRepository.saveAll(quizzes); // 일괄 저장
//
//        return ResponseEntity.ok("출제가 완료되었습니다.");
//    }


}
