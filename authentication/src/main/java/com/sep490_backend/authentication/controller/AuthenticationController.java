package com.sep490_backend.authentication.controller;

import com.sep490_backend.authentication.core.ApiRes;
import com.sep490_backend.authentication.entity.common.Tenant;
import com.sep490_backend.authentication.entity.tenant.User;
import com.sep490_backend.authentication.repository.common.TenantRepository;
import com.sep490_backend.authentication.repository.tenant.UserRepository;
import com.sep490_backend.authentication.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {

    private final JwtService jwtService;

    private final TenantRepository tenantRepository;

    private final UserRepository userRepository;

    private final Environment env;

    @GetMapping("verify")
    public ResponseEntity<ApiRes<Boolean>> verify(String token) {
        jwtService.verifyToken(token, true);
        return ResponseEntity.ok(ApiRes.success(true));
    }

    @GetMapping("test")
    public ResponseEntity<ApiRes<String>> test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Tenant> list1 = tenantRepository.findAll();
        List<User> list2 =userRepository.findAll();
        System.out.println(authentication.getPrincipal());
        return ResponseEntity.ok(ApiRes.success(env.getProperty("server.port")));
    }
}
