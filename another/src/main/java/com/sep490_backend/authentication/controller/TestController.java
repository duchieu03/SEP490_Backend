package com.sep490_backend.authentication.controller;

import com.sep490_backend.authentication.service.AnotherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/another")
public class TestController {
    @Autowired
    private AnotherService jwtService;

    @GetMapping
    public String helloWorld(){
        return jwtService.testAnotherService();
    }
}
