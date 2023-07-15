package com.example.market.service;

import com.example.market.domain.entity.Comment;
import com.example.market.domain.entity.Item;
import com.example.market.dto.comment.request.CommentCreateRequestDto;
import com.example.market.dto.comment.request.CommentDeleteRequestDto;
import com.example.market.dto.comment.request.CommentReplyRequestDto;
import com.example.market.dto.comment.request.CommentUpdateRequestDto;
import com.example.market.dto.comment.response.CommentListResponseDto;
import com.example.market.exception.ErrorCode;
import com.example.market.exception.MarketAppException;
import com.example.market.repository.CommentRepository;
import com.example.market.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.example.market.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void create(Long itemId, CommentCreateRequestDto dto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new MarketAppException(NOT_FOUND_ITEM, NOT_FOUND_ITEM.getMessage()));

        commentRepository.save(dto.toEntity(itemId));
    }

    public Page<CommentListResponseDto> readCommentList(Long itemId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

        Page<Comment> findCommentByAllItemId = commentRepository.findAllByItemId(itemId, pageable);

        Page<CommentListResponseDto> commentListResponseDto = findCommentByAllItemId.map(CommentListResponseDto::new);

        return commentListResponseDto;
    }

    @Transactional
    public void updateComment(Long itemId, Long commentId, CommentUpdateRequestDto dto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new MarketAppException(NOT_FOUND_ITEM, NOT_FOUND_ITEM.getMessage()));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new MarketAppException(NOT_FOUND_COMMENT, NOT_FOUND_COMMENT.getMessage()));

        validateItemIdMatch(item, comment);
        checkWriterAndPassword(dto.getWriter(), dto.getPassword(), comment);

        comment.update(dto);
    }

    @Transactional
    public void deleteComment(Long itemId, Long commentId, CommentDeleteRequestDto dto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new MarketAppException(NOT_FOUND_ITEM, NOT_FOUND_ITEM.getMessage()));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new MarketAppException(NOT_FOUND_COMMENT, NOT_FOUND_COMMENT.getMessage()));

        validateItemIdMatch(item, comment);
        checkWriterAndPassword(dto.getWriter(), dto.getPassword(), comment);

        commentRepository.delete(comment);
    }

    @Transactional
    public void updateCommentReply(Long itemId, Long commentId, CommentReplyRequestDto replyDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new MarketAppException(NOT_FOUND_ITEM, NOT_FOUND_ITEM.getMessage()));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new MarketAppException(NOT_FOUND_COMMENT, NOT_FOUND_COMMENT.getMessage()));

        validateItemIdMatch(item, comment);
        checkWriterAndPasswordForReply(replyDto, item);

        comment.updateCommentReply(replyDto);
    }

    private void checkWriterAndPasswordForReply(CommentReplyRequestDto replyDto, Item item) {
        if (!item.getWriter().equals(replyDto.getWriter())) {
            throw new MarketAppException(INVALID_WRITER, INVALID_WRITER.getMessage());
        }

        if (!item.getPassword().equals(replyDto.getPassword())) {
            throw new MarketAppException(INVALID_WRITER, INVALID_WRITER.getMessage());
        }
    }


    private void checkWriterAndPassword(String writer, String password, Comment comment) {
        if (!comment.getWriter().equals(writer)) {
            throw new MarketAppException(INVALID_WRITER, INVALID_WRITER.getMessage());
        }

        if (!comment.getPassword().equals(password)) {
            throw new MarketAppException(INVALID_WRITER, INVALID_WRITER.getMessage());
        }
    }

    private void validateItemIdMatch(Item item, Comment comment) {
        if (item.getId() != comment.getItemId()) {
            throw new MarketAppException(NOT_MATCH_ITEM_AND_COMMENT, NOT_MATCH_ITEM_AND_COMMENT.getMessage());
        }
    }

}
