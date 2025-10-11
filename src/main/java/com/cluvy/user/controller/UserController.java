package com.cluvy.user.controller;

import com.cluvy.user.client.AuthFeignClient;
import com.cluvy.user.dto.*;
import com.cluvy.user.entity.User;
import com.cluvy.user.entity.enums.AuthType;
import com.cluvy.user.entity.enums.Status;
import com.cluvy.user.repository.UserRepository;
import com.cluvy.user.response.ApiResponse;
import com.cluvy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthFeignClient authFeignClient;

    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = userService.signup(request);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/verify")
    public UserVerifyResponse verifyUser(@RequestBody UserVerifyRequest request) {
        return userService.verifyUser(request);
    }

    @PostMapping("/social")
    public boolean registerOrFindSocialUser(@RequestBody SocialUserInfo userInfo) {
        return userService.registerOrFindSocialUser(userInfo);
    }

    @DeleteMapping("/me")
    public ApiResponse<Void> deleteUser(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = authFeignClient.getUserIdFromToken(authorizationHeader);
        userService.deleteUser(userId);
        return ApiResponse.onSuccess(null);
    }

    @DeleteMapping("/me/kakao")
    public ApiResponse<Void> deleteKakaoUser(@RequestHeader("Authorization") String authorizationHeader) {
        String socialId = authFeignClient.getSocialIdFromToken(authorizationHeader);
        userService.deleteSocialUser(socialId);
        return ApiResponse.onSuccess(null);
    }

 /*   @PutMapping("/last-login")
    public ApiResponse<Void> updateLastLoginAt(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = authFeignClient.getUserIdFromToken(authorizationHeader);
        userService.updateLastLoginAt(userId);
        return ApiResponse.onSuccess(null);
    }*/

    @PutMapping("/last-login")
    public ApiResponse<Void> updateLastLoginAt(@RequestHeader("Authorization") String authorizationHeader) {
        // 이메일 회원
        try {
            Long userId = authFeignClient.getUserIdFromToken(authorizationHeader);
            userService.updateLastLoginAt(userId);
        } catch (Exception e) {
            // 카카오 회원
            String socialId = authFeignClient.getSocialIdFromToken(authorizationHeader);
            userService.updateLastLoginAtBySocialId(socialId);
        }
        return ApiResponse.onSuccess(null);
    }

    @PutMapping("/{id}/reactivate")
    public ApiResponse<Void> reactivateUser(@PathVariable Long id) {
        userService.reactivateUser(id);
        return ApiResponse.onSuccess(null);
    }
}
