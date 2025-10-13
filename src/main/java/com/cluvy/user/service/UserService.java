package com.cluvy.user.service;

import com.cluvy.user.dto.*;

public interface UserService {
    // 회원가입
    SignupResponse signup(SignupRequest request);
    // 회원 검증
    UserVerifyResponse verifyUser(UserVerifyRequest request);
    // 소셜 로그인 - 소셜 id로 회원 조회, 없으면 새 소셜 회원 등록
    boolean registerOrFindSocialUser(SocialUserInfo userInfo);
    // 회원 탈퇴
    void deleteUser(Long userId);
    void deleteSocialUser(String socialId);

    void updateLastLoginAt (Long userId);
    void updateLastLoginAtBySocialId(String socialId);

    void reactivateUser(Long userId);

    // 사용자 정보 가져오기
    UserInfoResponse getUserInfo(Long userId);
    UserInfoResponse getSocialUserInfo(String socialId);

    // 사용자 정보 수정하기
    UserInfoResponse updateUserInfo(Long userId, UserUpdateRequest request);
    UserInfoResponse updateSocialUserInfo(String socialId, UserUpdateRequest request);
}
