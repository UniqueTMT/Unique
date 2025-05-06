package com.unique.impl.gpt;

import com.unique.service.gpt.GptPromptService;
import org.springframework.stereotype.Service;

@Service
public class GptPromptServiceImpl implements GptPromptService {

    @Override
    public String svcBuildPrompt(String category, String chapter, String type, String count, String userPrompt, String text) {
        return String.format("""
    			당신은 JSON 형식으로만 응답하는 AI 문제 생성기입니다.

    			📂 카테고리: %s
    			📖 챕터: %s
    			🧾 문제 유형: %s (1: 객관식, 2: 주관식, 3: 혼합)
    			🔢 문제 개수: %s
    			🧠 출제자 추가 지시사항: %s


        ⚠️ 생성 조건:
        - 반드시 JSON 배열로만 응답하십시오. 그 외 설명 문장은 포함하지 마십시오.
        - 각 문제는 아래 형식을 따라야 하며, 모든 항목을 빠짐없이 작성하십시오.
        - 문제의 총점(`correctScore`)은 반드시 **100점**이 되도록 설정하십시오.

        👉 배점 규칙:
        - 객관식(`objYn`: "1") 문제는 문제 수에 따라 **균등하게 배점**하십시오.
        - 주관식(`objYn`: "0") 문제는 **객관식보다 더 높은 배점**을 부여하십시오. (예: 주관식 2문제 = 각 30점, 객관식 4문제 = 각 10점 등)
        - 혼합형(문제유형 3)인 경우, 객관식/주관식 간의 비율에 맞게 배점하십시오.

    			[
    			  {
    			    "quiz": "배열이란 무엇인가요?",
    			    "objYn": "1",
    			    "obj1": "선형",
    			    "obj2": "비선형",
    			    "obj3": "트리",
    			    "obj4": "그래프",
    			    "correctAnswer": "1",
    			    "correctScore": 10,
    			    "hint": "배열은 연속된 메모리를 사용합니다.",
    			    "comments": "기본 개념 평가용 문제입니다."
    			  }
    			]

    			[자료 시작]
    			%s
    			[자료 끝]
    			""", category, chapter, type, count, userPrompt, text);
    }
}
