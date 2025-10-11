package com.cluvy.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "http://localhost:8080")
public interface AuthFeignClient {
    @GetMapping("/api/auth/me")
    Long getUserIdFromToken(@RequestHeader("Authorization") String authorizationHeader);
    @GetMapping("/api/auth/me/kakao")
    String getSocialIdFromToken(@RequestHeader("Authorization") String authorizationHeader);

}
