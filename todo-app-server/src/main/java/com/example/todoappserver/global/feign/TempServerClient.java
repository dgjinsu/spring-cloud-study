package com.example.todoappserver.global.feign;

import com.example.todoappserver.domain.todo.dto.TodoResponse;
import com.example.todoappserver.domain.todo.message.dto.TodoSaveRequestDto;
import com.example.todoappserver.global.common.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "temp-server")
public interface TempServerClient {

    @PostMapping("/temp-server/api/v1/temps")
    ResponseEntity<Response> saveTemp(@RequestBody TodoResponse response);
}