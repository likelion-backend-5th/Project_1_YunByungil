package com.example.market.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentDeleteRequestDto {
    @NotBlank
    private String writer;
    @NotBlank
    private String password;

    @Builder
    public CommentDeleteRequestDto(String writer, String password) {
        this.writer = writer;
        this.password = password;
    }
}
