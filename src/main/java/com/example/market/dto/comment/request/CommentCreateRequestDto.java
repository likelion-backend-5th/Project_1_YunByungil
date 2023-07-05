package com.example.market.dto.comment.request;

import com.example.market.domain.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentCreateRequestDto {

    @NotBlank
    private String writer;
    @NotBlank
    private String password;
    @NotBlank
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
