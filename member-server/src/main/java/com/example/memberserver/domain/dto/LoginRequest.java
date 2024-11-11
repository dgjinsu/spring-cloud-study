package com.example.memberserver.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class LoginRequest {
    private String loginId;
    private String password;
}
