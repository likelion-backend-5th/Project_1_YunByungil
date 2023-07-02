package com.example.market.service;

import com.example.market.domain.entity.Comment;
import com.example.market.domain.entity.Item;
import com.example.market.dto.comment.request.CommentCreateRequestDto;
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