package com.example.memberserver.global.security;

import com.example.common.role.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class MemberDto {

    private Long id;
    private Role role;
}
