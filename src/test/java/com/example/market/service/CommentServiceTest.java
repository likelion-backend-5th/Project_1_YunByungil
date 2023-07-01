package com.example.market.service;

import com.example.market.domain.entity.Comment;
import com.example.market.dto.comment.request.CommentCreateRequestDto;
import com.example.market.repository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @DisplayName("댓글 생성 기능 테스트")
    @Test
    void createComment() {
        // given
        Long itemId = 3L;

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
        assertThat(comment.getItem_id()).isEqualTo(itemId);

    }
}