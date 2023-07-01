package com.example.market.controller;

import com.example.market.dto.comment.request.CommentCreateRequestDto;
import com.example.market.dto.comment.response.CommentResponseDto;
import com.example.market.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/items/{itemId}/comments")
    public CommentResponseDto create(@PathVariable Long itemId,
                                     @RequestBody CommentCreateRequestDto dto) {
        commentService.create(itemId, dto);

        return new CommentResponseDto("댓글이 등록되었습니다.");
    }
}
