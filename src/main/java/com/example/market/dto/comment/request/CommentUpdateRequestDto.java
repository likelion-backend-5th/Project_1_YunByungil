package com.example.market.dto.comment.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentUpdateRequestDto {

    private String writer;
    private String password;
    private String content;

    @Builder
    public CommentUpdateRequestDto(String writer, String password, String content) {
        this.writer = writer;
        this.password = password;
        this.content = content;
    }

}
