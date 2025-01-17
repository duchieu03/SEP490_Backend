package com.sep490_backend.migration.controller;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {
    @GetMapping
    public String test() {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(new HikariDataSource());
        return "test";
    }
}
