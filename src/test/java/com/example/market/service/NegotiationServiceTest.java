package com.example.market.service;

import com.example.market.domain.entity.Comment;
import com.example.market.domain.entity.Item;
import com.example.market.domain.entity.Negotiation;
import com.example.market.dto.negotiation.request.NegotiationCreateRequestDto;
import com.example.market.repository.CommentRepository;
import com.example.market.repository.ItemRepository;
import com.example.market.repository.NegotiationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    private Item createItem() {
        return itemRepository.save(Item.builder()
                .title("제목")
                .password("비밀번호")
                .minPriceWanted(10_000)
                .writer("작성자")
                .description("내용")
                .build());
    }


}