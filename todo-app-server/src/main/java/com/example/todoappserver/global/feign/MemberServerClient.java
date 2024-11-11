package com.example.todoappserver.global.feign;

import com.example.todoappserver.domain.todo.dto.member.MemberInfoResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "member-server")
public interface MemberServerClient {

    @GetMapping("/member-server/api/v1/members/info")
    @CircuitBreaker(name = "customCircuitBreaker", fallbackMethod = "getMemberFallback")
    MemberInfoResponse getMemberInfo();

    // Fallback 메소드 추가
    default MemberInfoResponse getMemberFallback(Throwable throwable) {
        System.out.println("Fallback executed due to: " + throwable.getMessage());
        return new MemberInfoResponse();
    }
}
