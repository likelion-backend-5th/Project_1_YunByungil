package com.example.market.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentUpdateRequestDto {

    @NotBlank
    private String content;

    @Builder
    public CommentUpdateRequestDto(String content) {
        this.content = content;
    }

}
