package com.unique.impl.gpt;

import com.unique.service.gpt.GptPromptService;
import org.springframework.stereotype.Service;

@Service
public class GptPromptServiceImpl implements GptPromptService {

    @Override
    public String svcBuildPrompt(String category, String chapter, String type, String count, String userPrompt, String text) {
        return String.format("""
        당신은 교육용 문제를 JSON 형식으로 생성하는 AI입니다.

        다음 조건을 참고해 문제를 완전히 생성하세요:

        📂 카테고리: %s
        📖 챕터: %s
        🧾 문제 유형: %s (1: 객관식, 2: 주관식, 3: 혼합)
        🔢 문제 개수: %s
        🧠 추가 지시사항: %s

        [출력 지침]
        - 반드시 JSON 배열 형식으로만 응답하세요. 그 외의 텍스트는 절대 포함하지 마세요.
        + 반드시 JSON 배열 형식([ ... ])로만 출력하세요.
        + 절대 어떤 말머리 설명도 붙이지 마세요. JSON 외 텍스트가 포함되면 안 됩니다.
        + 출력은 무조건 [ 로 시작하고 ] 로 끝나야 하며, 그 외의 설명은 입력하지 마세요.


        [예시]
        [
          {
            "quiz": "변수란 무엇인가요?",
            "objYn": "1",
            "obj1": "값을 저장하는 공간",
            "obj2": "함수의 이름",
            "obj3": "반복문",
            "obj4": "조건문",
            "correctAnswer": "1",
            "correctScore": 10,
            "hint": "변수는 데이터를 담는 공간입니다.",
            "comments": "객관식 문제입니다."
          }
        ]

        [학습자료 시작]
        %s
        [학습자료 끝]
        """, category, chapter, type, count, userPrompt, count, text);
    }

}
