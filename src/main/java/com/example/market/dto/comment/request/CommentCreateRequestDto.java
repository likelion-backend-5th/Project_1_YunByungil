package com.example.market.dto.comment.request;

import com.example.market.domain.entity.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentCreateRequestDto {

    private String writer;
    private String password;
    private String content;

    @Builder
    public CommentCreateRequestDto(String writer, String password, String content) {
        this.writer = writer;
        this.password = password;
        this.content = content;
    }

    public Comment toEntity(Long itemId) {
        return Comment.builder()
                .itemId(itemId)
                .writer(writer)
                .password(password)
                .content(content)
                .build();
    }
}
