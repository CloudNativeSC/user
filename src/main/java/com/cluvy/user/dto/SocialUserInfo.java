package com.cluvy.user.dto;

import lombok.Data;

@Data
public class SocialUserInfo {
    private String id;                // 카카오 회원번호
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String provider;          // "kakao"
}
