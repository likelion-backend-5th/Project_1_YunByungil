package com.example.market.controller;

import com.example.market.dto.comment.request.CommentCreateRequestDto;
import com.example.market.dto.comment.request.CommentUpdateRequestDto;
import com.example.market.dto.comment.response.CommentListResponseDto;
import com.example.market.dto.comment.response.CommentResponseDto;
import com.example.market.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/items/{itemId}/comments")
    public Page<CommentListResponseDto> readCommentList(@PathVariable Long itemId,
                                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        Page<CommentListResponseDto> commentListResponseDto = commentService.readCommentList(itemId, page, limit);
        return commentListResponseDto;
    }

    @PutMapping("/items/{itemId}/comments/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long itemId,
                                            @PathVariable Long commentId,
                                            @RequestBody CommentUpdateRequestDto dto) {
        commentService.updateComment(itemId, commentId, dto);

        return new CommentResponseDto("댓글이 수정되었습니다.");
    }
}
