package com.example.market.repository;

import com.example.market.domain.entity.Comment;
import com.example.market.dto.comment.request.CommentCreateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @AfterEach
    void end() {
        commentRepository.deleteAll();
    }

    @DisplayName("CommentRepository 생성 및 조회 테스트")
    @Test
    void createAndFindTest() {
        // given
        Comment comment = commentRepository.save(Comment.builder()
                .itemId(1L)
                .writer("작성자")
                .password("비밀번호")
                .content("댓글내용")
                .reply("답변대기중")
                .build());

        // when
        List<Comment> all = commentRepository.findAll();

        // then
        assertThat(all.get(0).getContent()).isEqualTo(comment.getContent());
    }

    @DisplayName("findAllByItemId() 메소드 테스트")
    @Test
    void testFindAllByItemId() {
        // given
        CommentCreateRequestDto dto = CommentCreateRequestDto.builder()
                .writer("댓글 작성자")
                .password("댓글 비번")
                .content("하잉")
                .build();

        Comment comment = null;
        for (int i = 0; i < 20; i++) {
            comment = commentRepository.save(dto.toEntity(1L));
        }

        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<Comment> findCommentByAllItemId = commentRepository.findAllByItemId(comment.getItemId(), pageable);

        // then
        assertThat(findCommentByAllItemId.hasNext()).isTrue();
        assertThat(findCommentByAllItemId.getTotalElements()).isEqualTo(20L); // 전체 데이터 수
        assertThat(findCommentByAllItemId.getSize()).isEqualTo(5);

    }

}