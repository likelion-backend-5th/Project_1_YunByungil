package com.example.market.dto.comment.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentReplyRequestDto {

    private String writer;
    private String password;
    private String reply;

    @Builder
    public CommentReplyRequestDto(String writer, String password, String reply) {
        this.writer = writer;
        this.password = password;
        this.reply = reply;
    }
}
