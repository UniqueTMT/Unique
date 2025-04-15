package com.unique.service.gpt;


public interface GptPromptService {
    String svcBuildPrompt(String category, String chapter, String type, String count, String userPrompt, String text);
}
