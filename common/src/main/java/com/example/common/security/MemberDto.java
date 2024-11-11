package com.example.common.security;

import com.example.common.role.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class MemberDto {

    private String loginId;
    private Role role;
}
