package com.sep490_backend.authentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnotherService {
   public String testAnotherService(){
       return "Hello Another";
   }
}
