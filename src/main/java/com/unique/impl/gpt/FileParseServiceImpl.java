package com.unique.impl.gpt;

import com.unique.service.gpt.FileParseService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileParseServiceImpl implements FileParseService {
    @Override
    public String svcExtractText(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            if (filename != null && filename.endsWith(".pdf")) {
                PDDocument doc = PDDocument.load(file.getInputStream());
                return new PDFTextStripper().getText(doc);
            }
            return "지원하지 않는 형식입니다.";
        } catch (IOException e) {
            throw new RuntimeException("파일 파싱 실패", e);
        }
    }
}
