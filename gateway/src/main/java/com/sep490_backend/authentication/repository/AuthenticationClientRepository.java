package com.sep490_backend.authentication.repository;

import com.sep490_backend.authentication.core.ApiRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "authentication-service")
public interface AuthenticationClientRepository {

    @GetMapping("authentication/verify")
    ResponseEntity<ApiRes<Boolean>> verify(String token);
}
