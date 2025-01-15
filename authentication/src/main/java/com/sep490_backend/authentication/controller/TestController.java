package com.sep490_backend.authentication.controller;

import com.sep490_backend.authentication.entity.Permission;
import com.sep490_backend.authentication.entity.Role;
import com.sep490_backend.authentication.entity.User;
import com.sep490_backend.authentication.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final JwtService jwtService;

    @GetMapping
    public String helloWorld(){
        Permission permission = Permission.builder()
                .name("testPer").build();

        Role role = new Role();
        role.setName("ADMIN");
        role.setPermissions(Set.of(permission));

        User testUser = new User();
        testUser.setUsername("HieuNDHE");
        testUser.setPassword("123456");
        testUser.setRoles(Set.of(role));

        String jwtToken = jwtService.generateToken(testUser);
         System.out.println(jwtToken);
        return "hello world";
    }

    @GetMapping("/test")
    public String test(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().toString();
    }
}
