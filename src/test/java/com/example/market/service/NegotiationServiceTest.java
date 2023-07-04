package com.example.market.service;

import com.example.market.domain.entity.Comment;
import com.example.market.domain.entity.Item;
import com.example.market.domain.entity.Negotiation;
import com.example.market.dto.negotiation.request.NegotiationCreateRequestDto;
import com.example.market.dto.negotiation.request.NegotiationDeleteRequestDto;
import com.example.market.dto.negotiation.request.NegotiationListRequestDto;
import com.example.market.dto.negotiation.request.NegotiationUpdateRequestDto;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @DisplayName("등록된 제안 판매자 기준 조회 메서드 테스트")
    @Test
    void readAllProposalsBySeller() {
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
    
    @DisplayName("등록된 제안 제안자 기준 조회 메서드")
    @Test
    void readAllProposalsByNegotiator() {
        // given
        Item item = createItem();
        createSixNegotiation(item.getId());
        createSixNegotiation2(item.getId());

        int page = 0;
        int limit = 5;

        NegotiationListRequestDto listDto = NegotiationListRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .build();

        // when
        Page<NegotiationListResponseDto> negotiationListResponseDto = negotiationService.readAllNegotiation(item.getId(), page, limit, listDto);

        // then
        assertThat(negotiationListResponseDto.getSize()).isEqualTo(5);
        assertThat(negotiationListResponseDto.getTotalElements()).isEqualTo(6);
        assertThat(negotiationListResponseDto.hasNext()).isTrue();
        
    }

    @DisplayName("등록된 제안 수정 테스트 (제안자 기준)")
    @Test
    void updateNegotiation() {
        // given
        final int updatePrice = 10_000;
        final String writer = "제안 수정 작성자";
        final String password = "제안 수정 비밀번호";
        final int price = 5_000;
        Item item = createItem();
        Negotiation negotiation = createNegotiationOne(item, writer, password, price);

        NegotiationUpdateRequestDto updateDto = NegotiationUpdateRequestDto.builder()
                .writer("제안 수정 작성자")
                .password("제안 수정 비밀번호")
                .suggestedPrice(updatePrice)
                .build();

        // when
        negotiationService.updateNegotiation(item.getId(), negotiation.getId(), updateDto);

        // then
        Negotiation findNegotiation = negotiationRepository.findById(negotiation.getId()).get();
        assertThat(findNegotiation.getSuggestedPrice()).isEqualTo(updatePrice);
    }

    @DisplayName("등록된 제안 수정 *예외* 테스트 (제안자 기준)")
    @Test
    void updateNegotiationException() {
        // given
        final int updatePrice = 10_000;
        final String writer = "제안 수정 작성자";
        final String password = "제안 수정 비밀번호";
        final int price = 5_000;
        Item item = createItem();
        Negotiation negotiation = createNegotiationOne(item, writer, password, price);

        NegotiationUpdateRequestDto updateDto = NegotiationUpdateRequestDto.builder()
                .writer("다른 작성자")
                .password("다른 비밀번호")
                .suggestedPrice(updatePrice)
                .build();
        // when
        assertThatThrownBy(() -> {
            negotiationService.updateNegotiation(item.getId(), negotiation.getId(), updateDto);
        }).isInstanceOf(ResponseStatusException.class);

        // then
    }

    @DisplayName("등록된 제안 삭제 기능 테스트")
    @Test
    void deleteNegotiation() {
        // given
        final int price = 10_000;
        final String writer = "제안 작성자";
        final String password = "제안 작성자";

        Item item = createItem();

        Negotiation negotiation = createNegotiationOne(item, writer, password, price);

        NegotiationDeleteRequestDto deleteDto = NegotiationDeleteRequestDto.builder()
                .writer(writer)
                .password(password)
                .build();

        // when
        negotiationService.deleteNegotiation(item.getId(), negotiation.getId(), deleteDto);

        // then
        List<Negotiation> all = negotiationRepository.findAll();

        assertThat(all.size()).isEqualTo(0);

    }

    @DisplayName("등록된 제안 삭제할 때 계정 정보 틀렸을 때 예외 발생")
    @Test
    void deleteNegotiationException() {
        // given
        final int price = 10_000;
        final String writer = "제안 작성자";
        final String password = "제안 작성자";

        Item item = createItem();

        Negotiation negotiation = createNegotiationOne(item, writer, password, price);
        final String otherWriter = "다른 작성자";
        final String otherPassword = "다른 비밀번호";

        NegotiationDeleteRequestDto deleteDto = NegotiationDeleteRequestDto.builder()
                .writer(otherWriter)
                .password(otherPassword)
                .build();

        // when
        assertThatThrownBy(() -> {
            negotiationService.deleteNegotiation(item.getId(), negotiation.getId(), deleteDto);
        }).isInstanceOf(ResponseStatusException.class);

        // then

    }

    @DisplayName("제안의 상태를 변경(제안 -> 수락)하는 메서드 테스트")
    @Test
    void updateNegotiationStatus() {
        // given
        Item item = createItem();
        
        final String writer = "제안 작성자";
        final String password = "제안 비밀번호";
        final int price = 5_000;
        Negotiation negotiation = createNegotiationOne(item, writer, password, price);

        NegotiationUpdateRequestDto statusDto = NegotiationUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .status("수락")
                .build();

        // when
        negotiationService.updateNegotiation(item.getId(), negotiation.getId(), statusDto);
        
        // then
        Negotiation findNegotiation = negotiationRepository.findAll().get(0);

        assertThat(findNegotiation.getStatus()).isEqualTo("수락");

    }
    @DisplayName("제안의 상태를 변경할 때 계정 정보 다르면 예외 발생 테스트")
    @Test
    void updateNegotiationStatusException() {
        // given
        Item item = createItem();

        final String writer = "제안 작성자";
        final String password = "제안 비밀번호";
        final int price = 5_000;
        Negotiation negotiation = createNegotiationOne(item, writer, password, price);

        NegotiationUpdateRequestDto statusDto = NegotiationUpdateRequestDto.builder()
                .writer("다른 물품 작성자")
                .password("다른 물품 비밀번호")
                .status("수락")
                .build();

        // when
        assertThatThrownBy(() -> {
            negotiationService.updateNegotiation(item.getId(), negotiation.getId(), statusDto);
        }).isInstanceOf(ResponseStatusException.class);

        // then

    }
    
    @DisplayName("자신이 등록한 제안이 수락 상태일 경우 구매 확정 테스트")
    @Test
    void changeStatusAccept() {
        // given
        Item item = createItem();

        final String writer = "제안 작성자";
        final String password = "제안 비밀번호";
        final int price = 5_000;
        Negotiation negotiation = createNegotiationOne(item, writer, password, price);

        NegotiationUpdateRequestDto statusDto = NegotiationUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .status("수락")
                .build();

        negotiationService.updateNegotiation(item.getId(), negotiation.getId(), statusDto);

        NegotiationUpdateRequestDto purchaseDto = NegotiationUpdateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .status("확정")
                .build();

        // when
        negotiationService.updateNegotiation(item.getId(), negotiation.getId(), purchaseDto);

        // then
        Negotiation status = negotiationRepository.findById(negotiation.getId()).get();

        assertThat(status.getStatus()).isEqualTo("확정");

    }
    @DisplayName("자신이 등록한 제안이 수락 상태가 아닌 상태에서 확정 요청시 예외 발생")
    @Test
    void changeStatusAcceptException() {
        // given
        Item item = createItem();

        final String writer = "제안 작성자";
        final String password = "제안 비밀번호";
        final int price = 5_000;
        Negotiation negotiation = createNegotiationOne(item, writer, password, price);

        NegotiationUpdateRequestDto statusDto = NegotiationUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .status("제안")
                .build();

        negotiationService.updateNegotiation(item.getId(), negotiation.getId(), statusDto);

        NegotiationUpdateRequestDto purchaseDto = NegotiationUpdateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .status("확정")
                .build();

        // when
        assertThatThrownBy(() -> {
            negotiationService.updateNegotiation(item.getId(), negotiation.getId(), purchaseDto);
        }).isInstanceOf(ResponseStatusException.class);

        // then

    }

    @DisplayName("구매 확정시 다른 제안은 거절로 변경")
    @Test
    void changeProposalToReject() {
        // given
        Item item = createItem();

        final String writer = "제안 작성자";
        final String password = "제안 비밀번호";

        final String otherWriter = "제안 작성자2";
        final String otherPassword = "제안 비밀번호2";
        final int price = 5_000;
        Negotiation negotiation = createNegotiationOne(item, writer, password, price);
        Negotiation otherNegotiation = createNegotiationOne(item, otherWriter, otherPassword, price);

        NegotiationUpdateRequestDto statusDto = NegotiationUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .status("수락")
                .build();

        negotiationService.updateNegotiation(item.getId(), negotiation.getId(), statusDto);

        NegotiationUpdateRequestDto purchaseDto = NegotiationUpdateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .status("확정")
                .build();

        // when
        negotiationService.updateNegotiation(item.getId(), negotiation.getId(), purchaseDto);

        // then
        Negotiation accept = negotiationRepository.findById(negotiation.getId()).get();

        Negotiation refuse = negotiationRepository.findById(otherNegotiation.getId()).get();

        assertThat(accept.getStatus()).isEqualTo("확정");
        assertThat(refuse.getStatus()).isEqualTo("거절");

    }
    
    @DisplayName("구매 확정시 그 아이템의 상태는 판매 완료로 변경")
    @Test
    void changeItemStatus() {
        // given
        Item item = createItem();

        final String writer = "제안 작성자";
        final String password = "제안 비밀번호";
        final int price = 5_000;
        Negotiation negotiation = createNegotiationOne(item, writer, password, price);

        NegotiationUpdateRequestDto statusDto = NegotiationUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .status("수락")
                .build();

        negotiationService.updateNegotiation(item.getId(), negotiation.getId(), statusDto);

        NegotiationUpdateRequestDto purchaseDto = NegotiationUpdateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .status("확정")
                .build();

        // when
        negotiationService.updateNegotiation(item.getId(), negotiation.getId(), purchaseDto);
        
        // then
        Item findItem = itemRepository.findById(item.getId()).get();

        assertThat(findItem.getStatus()).isEqualTo("판매 완료");

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

    private Negotiation createNegotiationOne(Item item, String writer, String password, int price) {
        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .writer(writer)
                .password(password)
                .suggestedPrice(5_000)
                .build();
        Negotiation negotiation = createDto.toEntity(item.getId());

        negotiationRepository.save(negotiation);

        return negotiation;
    }

    private void createSixNegotiation(Long itemId) {
        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .suggestedPrice(5_000)
                .build();

        for (int i = 0; i < 6; i++) {
            negotiationService.createNegotiation(itemId, createDto);
        }
    }
    private void createSixNegotiation2(Long itemId) {
        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .writer("제안 작성자2")
                .password("제안 비밀번호2")
                .suggestedPrice(5_000)
                .build();

        for (int i = 0; i < 6; i++) {
            negotiationService.createNegotiation(itemId, createDto);
        }
    }
}