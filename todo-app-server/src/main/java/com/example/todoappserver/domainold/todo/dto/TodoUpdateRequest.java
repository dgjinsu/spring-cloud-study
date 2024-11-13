package com.example.todoappserver.domainold.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TodoUpdateRequest(
        @Schema(description = "TODO 내용", example = "청소하기")
        String content,
        @Schema(description = "TODO 완료 여부", example = "true")
        Boolean isComplete
) {
}
