package com.example.market.repository;

import com.example.market.domain.entity.Comment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

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

}