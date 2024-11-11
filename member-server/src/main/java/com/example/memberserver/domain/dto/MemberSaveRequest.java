package com.example.memberserver.domain.dto;

import com.example.common.role.Role;

public record MemberSaveRequest(String loginId, String password, String name, Role role) {
}
