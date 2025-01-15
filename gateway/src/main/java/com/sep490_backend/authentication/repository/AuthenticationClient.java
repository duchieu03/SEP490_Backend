package com.sep490_backend.authentication.repository;

import com.sep490_backend.authentication.core.ApiRes;
import com.sep490_backend.authentication.dto.request.VerifyRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthenticationClient {
    @PostExchange(url = "/verify", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiRes<Boolean>> introspect(@RequestBody VerifyRequest request);
}
