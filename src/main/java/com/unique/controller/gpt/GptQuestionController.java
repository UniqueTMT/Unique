package com.unique.controller.gpt;

import com.unique.kafka.GptKafkaProducer;
import com.unique.service.gpt.FileParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptQuestionController {

    private final FileParseService fileParseService;
    private final GptKafkaProducer kafkaProducer;

    @PostMapping("/generate")
    public ResponseEntity<String> ctlGenerateQuestions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("prompt") String prompt) {

        String text = fileParseService.svcExtractText(file);
        kafkaProducer.sendQuestionRequest(text, prompt);
        return ResponseEntity.ok("문제 생성 요청 완료");
    }
}
