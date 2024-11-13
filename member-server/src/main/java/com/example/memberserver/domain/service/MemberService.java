package com.example.memberserver.domain.service;

import com.example.common.role.Role;
import com.example.memberserver.domain.dto.*;
import com.example.memberserver.domain.entity.Member;
import com.example.memberserver.domain.repository.MemberRepository;
import com.example.memberserver.global.exception.ErrorCode;
import com.example.memberserver.global.exception.MemberException;
import com.example.memberserver.global.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberResponse saveMember(MemberSaveRequest request) {
        Member member = Member.builder()
                .loginId(request.loginId())
                .password(encoder.encode(request.password()))
                .role(Role.ROLE_MEMBER)
                .name(request.name())
                .build();
        
        Member savedMember = memberRepository.save(member);

        return new MemberResponse(savedMember.getId(), savedMember.getName());
    }

    public LoginResponse login(LoginRequest request) {
        // id 체크
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // authCode 체크
        if (!encoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // accessToken & refreshToken 생성
        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getRole());

        return new LoginResponse(accessToken);
    }

    public MemberInfoResponse getInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberInfoResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .build();
    }
}
