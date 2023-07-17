package com.example.market.controller;

import com.example.market.dto.comment.request.CommentCreateRequestDto;
import com.example.market.dto.comment.request.CommentDeleteRequestDto;
import com.example.market.dto.comment.request.CommentReplyRequestDto;
import com.example.market.dto.comment.request.CommentUpdateRequestDto;
import com.example.market.dto.comment.response.CommentListResponseDto;
import com.example.market.dto.comment.response.CommentResponseDto;
import com.example.market.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static com.example.market.common.SystemMessage.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/items/{itemId}/comments")
    public CommentResponseDto create(@PathVariable Long itemId,
                                     @Valid @RequestBody CommentCreateRequestDto dto) {
        commentService.create(itemId, dto);

        return new CommentResponseDto(REGISTER_COMMENT);
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
                                            @Valid @RequestBody CommentUpdateRequestDto dto) {
        commentService.updateComment(itemId, commentId, dto);

        return new CommentResponseDto(UPDATE_COMMENT);
    }

    @DeleteMapping("/items/{itemId}/comments/{commentId}")
    public CommentResponseDto deleteComment(@PathVariable Long itemId,
                                            @PathVariable Long commentId,
                                            @Valid @RequestBody CommentDeleteRequestDto dto) {
        commentService.deleteComment(itemId, commentId, dto);

        return new CommentResponseDto(DELETE_COMMENT);
    }


    @PutMapping("/items/{itemId}/comments/{commentId}/reply")
    public CommentResponseDto updateCommentReply(@PathVariable Long itemId,
                                                 @PathVariable Long commentId,
                                                 @Valid @RequestBody CommentReplyRequestDto replyDto) {
        commentService.updateCommentReply(itemId, commentId, replyDto);

        return new CommentResponseDto(REGISTER_REPLY);
    }
}
