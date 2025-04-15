package com.unique.impl.gpt;

import com.unique.service.gpt.GptPromptService;
import org.springframework.stereotype.Service;

@Service
public class GptPromptServiceImpl implements GptPromptService {

    @Override
    public String svcBuildPrompt(String category, String chapter, String type, String count, String userPrompt, String text) {
        return String.format("""
            당신은 교육 전문가입니다. 다음 학습 자료를 바탕으로 문제를 생성해주세요.

            📂 카테고리: %s
            📖 챕터: %s
            🧾 문제 유형: %s  (1: 객관식, 2: 주관식, 3: 혼합)
            🔢 문제 개수: %s
            🧠 추가 지시사항: %s

                JSON 배열 형식으로 다음 항목을 포함해서 반환해주세요:
                - quiz
                - objYn ("1" = 객관식, "2" = 주관식, "3" = 혼합)
                - obj1 ~ obj4
                - correctAnswer
                - correctScore
                - hint
                - comments
                
                예시 형식:
                [
                  {
                    "quiz": "배열이란 무엇인가요?",
                    "objYn": "1",
                    "obj1": "선형 자료구조",
                    "obj2": "비선형 자료구조",
                    "obj3": "정렬된 트리",
                    "obj4": "링크드 리스트",
                    "correctAnswer": "1",
                    "correctScore": 10,
                    "hint": "배열은 메모리가 연속된 공간에 존재함",
                    "comments": "객관식 문제로 배열 구조에 대한 개념 테스트"
                  }
                ]
                

            [학습자료 시작]
            %s
            [학습자료 끝]

            ⚠️ 모든 항목은 JSON key-value 형식으로 정확하게 작성해주세요.
            """, category, chapter, type, count, userPrompt, text);
    }

}
