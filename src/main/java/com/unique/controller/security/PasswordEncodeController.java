package com.unique.controller.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * [Controller] 비밀번호 암호화 API 도구
 * - 암호화된 비밀번호를 출력해주는 간단한 API
 * - 사용방법 : http://localhost:5000/tool/encode?password=111
 */

@RestController
@RequestMapping("/tool")
@RequiredArgsConstructor
public class PasswordEncodeController {

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/encode")
    public String encodePassword(@RequestParam String password) {
        String encoded = passwordEncoder.encode(password);
        return "암호화된 비밀번호: " + encoded;
    }
}
