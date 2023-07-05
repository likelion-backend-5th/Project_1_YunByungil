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
    private String writer;
    @NotBlank
    private String password;
    @NotBlank
    private String content;

    @Builder
    public CommentUpdateRequestDto(String writer, String password, String content) {
        this.writer = writer;
        this.password = password;
        this.content = content;
    }

}
