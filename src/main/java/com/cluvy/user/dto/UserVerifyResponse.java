package com.cluvy.user.dto;

/*User 서비스가 Auth 서비스에 사용자 정보를 응답할 때 사용하는 데이터*/

import com.cluvy.user.entity.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVerifyResponse extends BaseTimeEntity {
    private Long id;
    private String email;
    private String username;
    private String passwordHash;
    private String birthDate;
    private String gender;
    private String profileImageUrl;
    private String authType; // "email", "kakao"
    private String socialId;
    private boolean emailVerified;
    private LocalDateTime emailVerifiedAt;
    private String status;
    private LocalDateTime lastLoginAt;
    private boolean termsAgreed;
    private String timezone;
    private String subscriptionType; // "free", "premium"
}
