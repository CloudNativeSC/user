package com.cluvy.user.dto;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String email;
    private String username;
    private String password;
    private String birthDate;
    private String gender;
    private String profileImageUrl;
    private boolean termsAgreed;
    private String timezone;
}
