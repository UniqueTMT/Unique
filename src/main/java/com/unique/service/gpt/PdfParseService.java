package com.unique.service.gpt;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PdfParseService {
    String svcExtractText(MultipartFile pdfFile) throws IOException;
}
