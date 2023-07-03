package com.example.market.service;

import com.example.market.domain.entity.Comment;
import com.example.market.domain.entity.Item;
import com.example.market.domain.entity.Negotiation;
import com.example.market.dto.negotiation.request.NegotiationCreateRequestDto;
import com.example.market.dto.negotiation.request.NegotiationListRequestDto;
import com.example.market.dto.negotiation.response.NegotiationListResponseDto;
import com.example.market.repository.CommentRepository;
import com.example.market.repository.ItemRepository;
import com.example.market.repository.NegotiationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NegotiationServiceTest {

    @Autowired
    NegotiationService negotiationService;

    @Autowired
    NegotiationRepository negotiationRepository;

    @Autowired
    ItemRepository itemRepository;

    @AfterEach
    void end() {
        negotiationRepository.deleteAll();
    }

    @DisplayName("제안 등록 기능 테스트")
    @Test
    void createProposal() {
        // given
        final String defaultStatus = "제안";
        Item item = createItem();

        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .suggestedPrice(5_000)
                .build();

        // when
        Long negotiationId = negotiationService.createNegotiation(item.getId(), createDto);

        // then
        Negotiation negotiation = negotiationRepository.findById(negotiationId).get();

        assertThat(negotiation.getStatus()).isEqualTo(defaultStatus);
        assertThat(negotiation.getSuggestedPrice()).isEqualTo(5_000);

    }

    @DisplayName("등록된 제안 단순 확인 조회 메서드 테스트")
    @Test
    void readAllProposals() {
        // given
        Item item = createItem();
        createSixNegotiation(item.getId());

        int page = 0;
        int limit = 5;

        NegotiationListRequestDto listDto = NegotiationListRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .build();
        // when
        Page<NegotiationListResponseDto> negotiationListResponseDto = negotiationService.readAllNegotiation(item.getId(), page, limit, listDto);

        // then
        assertThat(negotiationListResponseDto.getSize()).isEqualTo(5);
        assertThat(negotiationListResponseDto.getTotalElements()).isEqualTo(6);
        assertThat(negotiationListResponseDto.hasNext()).isTrue();

    }

    private Item createItem() {
        return itemRepository.save(Item.builder()
                .title("제목")
                .password("비밀번호")
                .minPriceWanted(10_000)
                .writer("작성자")
                .description("내용")
                .build());
    }

    private void createSixNegotiation(Long itemId) {
        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .suggestedPrice(5_000)
                .build();

        for (int i = 0; i < 6; i++) {
            negotiationService.createNegotiation(itemId, createDto);
        }
    }
}