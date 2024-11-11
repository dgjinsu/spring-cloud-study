package com.example.memberserver.domain.controller;

import com.example.common.annotation.LoginMember;
import com.example.memberserver.domain.dto.LoginRequest;
import com.example.memberserver.domain.dto.MemberInfoResponse;
import com.example.memberserver.domain.dto.MemberSaveRequest;
import com.example.memberserver.domain.service.MemberService;
import com.example.memberserver.global.common.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member-server/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity<Response> saveTemp(@RequestBody MemberSaveRequest request) {
        return ResponseEntity.ok(new Response(memberService.saveMember(request), "Member 저장 완료"));
    }

    @PostMapping("login")
    public ResponseEntity<Response> saveTemp(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(new Response(memberService.login(request), "로그인 완료"));
    }

    @GetMapping("/info")
    public MemberInfoResponse getInfo(@LoginMember String loginId, HttpServletRequest request) {
        Map<String, String> headers = getHeadersInfo(request);
        headers.forEach((key, value) -> System.out.println(key + ": " + value));
        return memberService.getInfo(loginId);
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headers.put(key, value);
        }
        return headers;
    }
}
