package com.cluvy.user.entity;

import com.cluvy.user.entity.common.BaseTimeEntity;
import com.cluvy.user.entity.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    private String passwordHash;

    private String birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profileImageUrl;

    private boolean termsAgreed;

    @Enumerated(EnumType.STRING)
    private Timezone timezone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthType authType = AuthType.EMAIL;

    private String socialId;
    private boolean emailVerified = false;
    private LocalDateTime emailVerifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Status status = Status.ACTIVE;

    private LocalDateTime lastLoginAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SubscriptionType subscriptionType = SubscriptionType.FREE;

    private LocalDateTime deletedAt;

    @Column(length = 255, nullable = false)
    private String bio = "아직 자기소개를 입력하지 않았어요.";

    //private String providerUserId;
}
