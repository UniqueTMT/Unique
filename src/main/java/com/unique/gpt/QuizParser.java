package com.unique.gpt;
import com.unique.dto.quiz.QuizDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QuizParser {
//    1. 자바에서 변수란 무엇인가?
//    A) 데이터 저장 공간  B) 연산자  C) 클래스  D) 객체
//    정답: A
//    힌트: 변수는 값을 저장하는 메모리 공간이다.
//            해설: 변수는 프로그래밍에서 데이터를 저장하고 참조하기 위한 식별자입니다.
public List<QuizDTO> parse(String gptResponse) {
    List<QuizDTO> result = new ArrayList<>();

    String[] questions = gptResponse.split("\\n(?=\\d+\\.)"); // 번호로 시작하는 문제 단위로 분할

    for (String block : questions) {
        QuizDTO dto = new QuizDTO();

        Matcher questionMatcher = Pattern.compile("\\d+\\.\\s*(.*)").matcher(block);
        Matcher choiceMatcher = Pattern.compile("[A-D]\\)\\s*(.*)").matcher(block);
        Matcher answerMatcher = Pattern.compile("정답[:：]\\s*([A-D])").matcher(block);
        Matcher hintMatcher = Pattern.compile("힌트[:：]\\s*(.*)").matcher(block);
        Matcher commentMatcher = Pattern.compile("해설[:：]\\s*(.*)").matcher(block);

        if (questionMatcher.find()) dto.setQuiz(questionMatcher.group(1));

        int i = 1;
        while (choiceMatcher.find()) {
            switch (i++) {
                case 1 -> dto.setObj1(choiceMatcher.group(1));
                case 2 -> dto.setObj2(choiceMatcher.group(1));
                case 3 -> dto.setObj3(choiceMatcher.group(1));
                case 4 -> dto.setObj4(choiceMatcher.group(1));
            }
        }

        if (answerMatcher.find()) dto.setCorrectAnswer(answerMatcher.group(1));
        if (hintMatcher.find()) dto.setHint(hintMatcher.group(1));
        if (commentMatcher.find()) dto.setComments(commentMatcher.group(1));

        dto.setObjYn('Y'); // 객관식 고정값

        result.add(dto);
    }

    return result;
}

}
