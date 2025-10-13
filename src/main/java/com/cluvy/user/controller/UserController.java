package com.cluvy.user.controller;

import com.cluvy.user.client.AuthFeignClient;
import com.cluvy.user.dto.*;
import com.cluvy.user.response.ApiResponse;
import com.cluvy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthFeignClient authFeignClient;

    @GetMapping("/health")
    public ApiResponse<?> health() {
        return ApiResponse.onSuccess("살아있음");
    }

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

    @GetMapping("/me")
    public UserInfoResponse getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = authFeignClient.getUserIdFromToken(authorizationHeader);
        return userService.getUserInfo(userId);
    }

    @GetMapping("/me/kakao")
    public UserInfoResponse getSocialUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String socialId = authFeignClient.getSocialIdFromToken(authorizationHeader);
        return userService.getSocialUserInfo(socialId);
    }

    @PatchMapping("/me")
    public UserInfoResponse patchUserInfo(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserUpdateRequest request) {
        Long userId = authFeignClient.getUserIdFromToken(authorizationHeader);
        return userService.updateUserInfo(userId, request);
    }

    @PatchMapping("/me/kakao")
    public UserInfoResponse patchSocialUserInfo(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserUpdateRequest request) {
        String socialId = authFeignClient.getSocialIdFromToken(authorizationHeader);
        return userService.updateSocialUserInfo(socialId, request);
    }
}
