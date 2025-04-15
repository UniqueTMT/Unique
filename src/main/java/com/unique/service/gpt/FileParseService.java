package com.unique.service.gpt;

import org.springframework.web.multipart.MultipartFile;

public interface FileParseService {
    String svcExtractText(MultipartFile file);
}
