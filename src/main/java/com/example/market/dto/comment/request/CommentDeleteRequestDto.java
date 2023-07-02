package com.example.market.dto.comment.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentDeleteRequestDto {
    private String writer;
    private String password;

    @Builder
    public CommentDeleteRequestDto(String writer, String password) {
        this.writer = writer;
        this.password = password;
    }
}
