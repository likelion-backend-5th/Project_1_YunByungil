package com.example.market.service;

import com.example.market.domain.entity.Comment;
import com.example.market.domain.entity.Item;
import com.example.market.dto.comment.request.CommentCreateRequestDto;
import com.example.market.dto.comment.request.CommentDeleteRequestDto;
import com.example.market.dto.comment.request.CommentUpdateRequestDto;
import com.example.market.dto.comment.response.CommentListResponseDto;
import com.example.market.repository.CommentRepository;
import com.example.market.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ItemRepository itemRepository;

    @AfterEach
    void end() {
        itemRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @DisplayName("댓글 생성 기능 테스트")
    @Test
    void createComment() {
        // given
        Long itemId = createItem();

        CommentCreateRequestDto dto = CommentCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .content("내용")
                .build();

        commentService.create(itemId, dto);

        // when
        List<Comment> all = commentRepository.findAll();
        Comment comment = all.get(0);

        // then
        assertThat(comment.getWriter()).isEqualTo(dto.getWriter());
        assertThat(comment.getPassword()).isEqualTo(dto.getPassword());
        assertThat(comment.getContent()).isEqualTo(dto.getContent());
        assertThat(comment.getItemId()).isEqualTo(itemId);
    }
    
    @DisplayName("존재하지 않는 물품에 댓글 작성시 예외 발생")
    @Test
    void notExistItemException() {
        // given
        Long itemId = 3L;

        CommentCreateRequestDto dto = CommentCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .content("내용")
                .build();

        // when
        assertThatThrownBy(() -> {
            commentService.create(itemId, dto);
        }).isInstanceOf(ResponseStatusException.class);
        
        // then
        
    }

    @DisplayName("댓글 조회 메서드 테스트")
    @Test
    void readCommentList() {
        // given
        Long itemId = createItem();

        CommentCreateRequestDto dto = CommentCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .content("내용")
                .build();

        for (int i = 0; i < 6; i++) {
            commentService.create(itemId, dto);
        }

        // when
        Page<CommentListResponseDto> commentListResponseDto = commentService.readCommentList(itemId, 0, 5);

        // then
        assertThat(commentListResponseDto.getTotalElements()).isEqualTo(6L);
        assertThat(commentListResponseDto.getSize()).isEqualTo(5);

    }

    @DisplayName("댓글 조회시, 존재하지 않는 경우 테스트")
    @Test
    void notExistComment() {
        // given

        // when
        Page<CommentListResponseDto> commentListResponseDto = commentService.readCommentList(5000L, 0, 5);

        // then
        assertThat(commentListResponseDto.getTotalElements()).isEqualTo(0L);

    }

    @DisplayName("댓글 수정 테스트")
    @Test
    void updateComment() {
        // given
        Long itemId = createItem();

        Long commentId = createComment(itemId);

        CommentUpdateRequestDto dto = CommentUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .content("수정완료")
                .build();


        // when
        commentService.updateComment(itemId, commentId, dto);

        // then
        Comment comment = commentRepository.findById(commentId).get();

        assertThat(comment.getContent()).isEqualTo(dto.getContent());

    }

    @DisplayName("댓글 수정할 때 itemId 값과 comment.getItemId 값이 다를 때 예외 발생")
    @Test
    void validateItemIdMatchForUpdate() {
        // given
        Long itemId = createItem();

        Long commentId = createComment(itemId);

        CommentUpdateRequestDto dto = CommentUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .content("수정완료")
                .build();

        // when
        assertThatThrownBy(() -> {
            commentService.updateComment(itemId + 120, commentId, dto);
        }).isInstanceOf(ResponseStatusException.class);

        // then
    }

    @DisplayName("댓글 수정할 때 Writer Or Password 다를 때 예외 발생")
    @Test
    void checkWriterAndPasswordForUpdate() {
        // given
        Long itemId = createItem();

        Long commentId = createComment(itemId);

        CommentUpdateRequestDto dontMatchWriter = CommentUpdateRequestDto.builder()
                .writer("기존 작성자랑 값이 다름")
                .password("비밀번호")
                .content("수정완료")
                .build();

        CommentUpdateRequestDto dontMatchPassword = CommentUpdateRequestDto.builder()
                .writer("작성자")
                .password("기존 비밀번호랑 값이 다름")
                .content("수정완료")
                .build();

        // when
        assertThatThrownBy(() -> { // 1. 작성자가 다른 경우
            commentService.updateComment(itemId, commentId, dontMatchWriter);
        }).isInstanceOf(ResponseStatusException.class);

        assertThatThrownBy(() -> { // 2. 패스워드가 다른 경우
            commentService.updateComment(itemId, commentId, dontMatchPassword);
        }).isInstanceOf(ResponseStatusException.class);

        // then


    }

    @DisplayName("댓글 삭제 기능 테스트")
    @Test
    void deleteComment() {
        // given
        Long itemId = createItem();
        Long commentId = createComment(itemId);

        CommentDeleteRequestDto deleteDto = CommentDeleteRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .build();

        // when
        commentService.deleteComment(itemId, commentId, deleteDto);

        // then
        List<Comment> all = commentRepository.findAll();
        assertThat(all.size()).isEqualTo(0);

    }

    @DisplayName("댓글 삭제할 때 itemId 값과 comment.getItemId 값이 다를 때 예외 발생")
    @Test
    void validateItemIdMatchForDelete() {
        // given
        Long itemId = createItem();
        Long notSameItemId = itemId + 121;

        Long commentId = createComment(itemId);

        CommentDeleteRequestDto deleteDto = CommentDeleteRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .build();

        // when
        assertThatThrownBy(() -> {
            commentService.deleteComment(notSameItemId, commentId, deleteDto);
        }).isInstanceOf(ResponseStatusException.class);

        // then
    }

    @DisplayName("댓글 수정할 때 Writer Or Password 다를 때 예외 발생")
    @Test
    void checkWriterAndPasswordForDelete() {
        // given
        Long itemId = createItem();

        Long commentId = createComment(itemId);

        CommentDeleteRequestDto dontMatchWriter = CommentDeleteRequestDto.builder()
                .writer("기존 작성자랑 값이 다름")
                .password("비밀번호")
                .build();

        CommentDeleteRequestDto dontMatchPassword = CommentDeleteRequestDto.builder()
                .writer("작성자")
                .password("기존 비밀번호랑 값이 다름")
                .build();

        // when
        assertThatThrownBy(() -> {
            commentService.deleteComment(itemId, commentId, dontMatchWriter);
        }).isInstanceOf(ResponseStatusException.class);

        assertThatThrownBy(() -> {
            commentService.deleteComment(itemId, commentId, dontMatchPassword);
        }).isInstanceOf(ResponseStatusException.class);

        // then

    }

    private Long createComment(Long itemId) {
        CommentCreateRequestDto dto = CommentCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .content("내용")
                .build();

        Comment comment = commentRepository.save(dto.toEntity(itemId));

        return comment.getId();
    }

    private Long createItem() {
        Item item = itemRepository.save(Item.builder()
                .title("제목")
                .minPriceWanted(10_000)
                .description("내용")
                .writer("작성자")
                .password("비밀번호")
                .build());

        return item.getId();
    }
}