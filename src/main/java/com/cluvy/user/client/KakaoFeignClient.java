package com.cluvy.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoClient", url = "https://kapi.kakao.com")
public interface KakaoFeignClient {
    @PostMapping(value = "/v1/user/unlink", consumes = "application/x-www-form-urlencoded")
    void unlinkWithAdminKey(
            @RequestHeader("Authorization") String adminKey,
            @RequestHeader("Content-Type") String contentType,
            @RequestParam("target_id_type") String targetIdType,
            @RequestParam("target_id") String targetId
    );
}
