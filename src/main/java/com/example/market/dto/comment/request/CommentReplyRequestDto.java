package com.example.market.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentReplyRequestDto {

    @NotBlank
    private String writer;
    @NotBlank
    private String password;
    @NotBlank
    private String reply;

    @Builder
    public CommentReplyRequestDto(String writer, String password, String reply) {
        this.writer = writer;
        this.password = password;
        this.reply = reply;
    }
}
