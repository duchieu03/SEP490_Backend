package com.sep490_backend.authentication.controller;

import com.sep490_backend.authentication.core.ApiRes;
import com.sep490_backend.authentication.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {

    private final JwtService jwtService;

    private final Environment env;

    @GetMapping("verify")
    public ResponseEntity<ApiRes<Boolean>> verify(String token) {
        jwtService.verifyToken(token, true);
        return ResponseEntity.ok(ApiRes.success(true));
    }

    @GetMapping("test")
    public ResponseEntity<ApiRes<String>> test() {
        return ResponseEntity.ok(ApiRes.success(env.getProperty("server.port")));
    }
}
