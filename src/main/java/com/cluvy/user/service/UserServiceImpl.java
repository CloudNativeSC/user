package com.cluvy.user.service;

import com.cluvy.user.client.KakaoFeignClient;
import com.cluvy.user.dto.*;
import com.cluvy.user.entity.User;
import com.cluvy.user.entity.enums.*;
import com.cluvy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final KakaoFeignClient kakaoFeignClient;

    @Value("${kakao.admin-key}")
    private String kakaoAdminKey;

    @Override
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 닉네임입니다.");
        }
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .birthDate(request.getBirthDate())
                .gender(request.getGender() != null ? Gender.valueOf(request.getGender().toUpperCase()) : null)
                .profileImageUrl(request.getProfileImageUrl())
                .termsAgreed(request.isTermsAgreed())
                .timezone(request.getTimezone() != null ? Timezone.valueOf(request.getTimezone().toUpperCase()) : null)
                .authType(AuthType.EMAIL)
                .status(Status.ACTIVE)
                .subscriptionType(SubscriptionType.FREE)
                .build();
        userRepository.save(user);
        return new SignupResponse(user.getId(), user.getEmail(), user.getUsername());
    }

    @Override
    public UserVerifyResponse verifyUser(UserVerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);
        if (user == null) {
            return null;
        }
        return UserVerifyResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .passwordHash(user.getPasswordHash())
                .birthDate(user.getBirthDate())
                .gender(user.getGender() != null ? user.getGender().name() : null)
                .profileImageUrl(user.getProfileImageUrl())
                .authType(user.getAuthType() != null ? user.getAuthType().name() : null)
                .socialId(user.getSocialId())
                .emailVerified(user.isEmailVerified())
                .emailVerifiedAt(user.getEmailVerifiedAt())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .lastLoginAt(user.getLastLoginAt())
                .termsAgreed(user.isTermsAgreed())
                .timezone(user.getTimezone() != null ? user.getTimezone().name() : null)
                .subscriptionType(user.getSubscriptionType() != null ? user.getSubscriptionType().name() : null)
                .build();
    }

    @Override
    public boolean registerOrFindSocialUser(SocialUserInfo userInfo) {
        Optional<User> userOpt = userRepository.findBySocialId(userInfo.getId());
       /* if (userOpt.isPresent()) {
            return false; // 기존 회원
        }*/
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getStatus() == Status.INACTIVE) {      // 복구
                user.setStatus(Status.ACTIVE);
                user.setDeletedAt(null);
                userRepository.save(user);
            }

            return false;                                   // 기존 회원
        }

        User user = User.builder()
                .email(userInfo.getEmail())
                .username(userInfo.getNickname())
                .socialId(userInfo.getId())
                .authType(AuthType.KAKAO)
                .profileImageUrl(userInfo.getProfileImageUrl())
                .status(Status.ACTIVE)
                .build();
        userRepository.save(user);
        return true; // 신규 회원
    }

    // 이메일 회원 탈퇴
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("User not found or already inactive"));

        // 소프트 딜리트
        user.setStatus(Status.INACTIVE);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // 카카오 소셜 회원 탈퇴
    @Override
    @Transactional
    public void deleteSocialUser(String socialId) {
        User user = userRepository.findBySocialIdAndStatus(socialId, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("User not found or already inactive"));

        // 카카오 연동 해제 (Admin Key 방식)
        kakaoFeignClient.unlinkWithAdminKey(
                "KakaoAK " + kakaoAdminKey.trim(),
                "application/x-www-form-urlencoded;charset=utf-8",
                "user_id",
                String.valueOf(user.getSocialId())
        );

        // 소프트 딜리트
        user.setStatus(Status.INACTIVE);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateLastLoginAt(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateLastLoginAtBySocialId(String socialId) {
        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void reactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(Status.ACTIVE);
        user.setDeletedAt(null);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }



}
