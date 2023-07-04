package com.example.market.repository;

import com.example.market.domain.entity.Comment;
import com.example.market.domain.entity.Negotiation;
import com.example.market.dto.comment.request.CommentCreateRequestDto;
import com.example.market.dto.negotiation.request.NegotiationCreateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
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
class NegotiationRepositoryTest {

    @Autowired
    NegotiationRepository repository;

    @AfterEach
    void end() {
        repository.deleteAll();
    }

    @DisplayName("Negotiation Repository 생성 및 조회 테스트")
    @Test
    void createAndFind() {
        // given
        final String defaultStatus = "제안";
        repository.save(Negotiation.builder()
                .writer("작성자")
                .suggestedPrice(10_000)
                .itemId(1L)
                .password("비밀번호")
                .build());

        // when
        List<Negotiation> all = repository.findAll();
        Negotiation negotiation = all.get(0);

        // then
        assertThat(negotiation.getItemId()).isEqualTo(1L);
        assertThat(negotiation.getStatus()).isEqualTo(defaultStatus);
    }

    @DisplayName("findAllByItemId() 메소드 테스트")
    @Test
    void testFindAllByItemId() {
        // given
        final Long itemId = 1L;

        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .suggestedPrice(10_000)
                .writer("작성자")
                .password("비밀번호")
                .build();

        Negotiation negotiation = null;
        for (int i = 0; i < 20; i++) {
            negotiation = repository.save(createDto.toEntity(itemId));
        }

        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<Negotiation> findNegotiationByAllItemId = repository.findAllByItemId(itemId, pageable);

        // then
        assertThat(findNegotiationByAllItemId.hasNext()).isTrue();
        assertThat(findNegotiationByAllItemId.getTotalElements()).isEqualTo(20L); // 전체 데이터 수
        assertThat(findNegotiationByAllItemId.getSize()).isEqualTo(5);

    }

    @DisplayName("findAllByItemIdAndWriterAndPassword() 메서드 테스트")
    @Test
    void findAllByItemAndWriterAndPassword() {
        // given
        final Long itemId = 1L;
        final int page = 0;
        final int limit = 5;
        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .suggestedPrice(10_000)
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .build();

        Negotiation negotiation = null;
        for (int i = 0; i < 5; i++) {
            negotiation = repository.save(createDto.toEntity(itemId));
        }

        NegotiationCreateRequestDto createDto2 = NegotiationCreateRequestDto.builder()
                .suggestedPrice(10_000)
                .writer("제안 작성자2")
                .password("제안 비밀번호2")
                .build();

        Negotiation negotiation2 = null;
        for (int i = 0; i < 5; i++) {
            negotiation2 = repository.save(createDto2.toEntity(itemId));
        }

        Pageable pageable = PageRequest.of(page, limit);

        // when
        Page<Negotiation> allByItemIdAndWriterAndPassword =
                repository.findAllByItemIdAndWriterAndPassword(itemId, negotiation.getWriter(), negotiation.getPassword(), pageable);

        // then
        assertThat(allByItemIdAndWriterAndPassword.getTotalElements()).isEqualTo(5);

    }

    @DisplayName("findByItemId() 메서드 테스트")
    @Test
    void findByItemId() {
        // given
        final Long itemId = 1L;
        final int price = 12_345;
        final int price2 = 54_321;
        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .suggestedPrice(price)
                .build();
        NegotiationCreateRequestDto createDto2 = NegotiationCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .suggestedPrice(price2)
                .build();

        repository.save(createDto.toEntity(itemId));
        repository.save(createDto2.toEntity(itemId));

        // when
        Negotiation negotiation = repository.findAll().get(0);
        Negotiation negotiation2 = repository.findAll().get(1);

        // then
        assertThat(repository.existsByItemId(itemId)).isTrue();
        assertThat(negotiation.getSuggestedPrice()).isEqualTo(price);
        assertThat(negotiation2.getSuggestedPrice()).isEqualTo(price2);

    }

    @DisplayName("구매가 확정되었을 때 그 구매 제안을 제외한 나머지 제안은 거절로 변경하는 메서드 테스트")
    @Test
    void updateNegotiationStatus() {
        // given
        final Long itemId = 1L;
        Negotiation accept = repository.save(Negotiation.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .itemId(itemId)
                .build());

        Negotiation negotiation = repository.save(Negotiation.builder()
                .writer("제안 작성자2")
                .password("제안 비밀번호2")
                .itemId(itemId)
                .build());

        // when
        assertThat(negotiation.getStatus()).isEqualTo("제안");

        repository.updateNegotiationStatus(accept.getId(), itemId);

        // then
        Negotiation refuse = repository.findById(negotiation.getId()).get();

        assertThat(refuse.getStatus()).isEqualTo("거절");


    }
}