package com.cluvy.user.dto;

/* 순수하게 사용자 정보만 다루는 DTO */

import com.cluvy.user.entity.common.BaseTimeEntity;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse extends BaseTimeEntity {
    private Long id;
    private String email;
    private String username;
    private String birthDate;
    private String gender;
    private String profileImageUrl;
    private String authType; // "email", "kakao"
    private String socialId;
    private boolean emailVerified;
    private LocalDateTime emailVerifiedAt;
    private String status;
    private LocalDateTime lastLoginAt;
    private String timezone;
    private String subscriptionType; // "free", "premium"
    private String bio;
}

