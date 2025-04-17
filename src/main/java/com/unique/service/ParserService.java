package com.unique.service;

import org.springframework.web.multipart.MultipartFile;

public interface ParserService {
    String extractText(MultipartFile file);


}

