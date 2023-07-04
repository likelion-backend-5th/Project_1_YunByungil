package com.example.market.service;

import com.example.market.domain.entity.Item;
import com.example.market.domain.entity.Negotiation;
import com.example.market.dto.negotiation.request.*;
import com.example.market.dto.negotiation.response.NegotiationListResponseDto;
import com.example.market.dto.negotiation.response.NegotiationResponseDto;
import com.example.market.repository.ItemRepository;
import com.example.market.repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NegotiationService {

    private final NegotiationRepository negotiationRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long createNegotiation(Long itemId, NegotiationCreateRequestDto dto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return negotiationRepository.save(dto.toEntity(itemId)).getId();
    }

    public Page<NegotiationListResponseDto> readAllNegotiation(Long itemId,
                                                               int page,
                                                               int limit,
                                                               NegotiationListRequestDto listDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

        if (isSeller(listDto.getWriter(), listDto.getPassword(), item)) {
            return getNegotiationListResponseDtoBySeller(itemId, pageable);
        }

        return getNegotiationListResponseDtoByBuyer(itemId, listDto, pageable);
    }

    @Transactional
    public NegotiationResponseDto updateNegotiation(Long itemId, Long negotiationId, NegotiationUpdateRequestDto updateDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Negotiation negotiation = negotiationRepository.findById(negotiationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (isSeller(updateDto.getWriter(), updateDto.getPassword(), item)) {
            negotiation.updateNegotiationStatus(updateDto.getStatus());
            return new NegotiationResponseDto("제안의 상태가 변경되었습니다.");
        }

        if (isBuyer(updateDto.getWriter(), updateDto.getPassword(), negotiation)) {
            negotiation.updateNegotiation(updateDto.getSuggestedPrice());
            return new NegotiationResponseDto("제안이 수정되었습니다.");
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public void deleteNegotiation(Long itemId, Long negotiationId, NegotiationDeleteRequestDto deleteDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Negotiation negotiation = negotiationRepository.findById(negotiationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!isBuyer(deleteDto.getWriter(), deleteDto.getPassword(), negotiation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        negotiationRepository.delete(negotiation);
    }

    private Page<NegotiationListResponseDto> getNegotiationListResponseDtoByBuyer(Long itemId, NegotiationListRequestDto listDto, Pageable pageable) {
        Page<Negotiation> allByItemIdAndWriterAndPassword =
                negotiationRepository.findAllByItemIdAndWriterAndPassword(itemId, listDto.getWriter(), listDto.getPassword(), pageable);

        Page<NegotiationListResponseDto> negotiationListResponseDto = allByItemIdAndWriterAndPassword.map(NegotiationListResponseDto::new);

        return negotiationListResponseDto;
    }

    private Page<NegotiationListResponseDto> getNegotiationListResponseDtoBySeller(Long itemId, Pageable pageable) {
        Page<Negotiation> allByItemId = negotiationRepository.findAllByItemId(itemId, pageable);

        Page<NegotiationListResponseDto> listResponseDto = allByItemId.map(NegotiationListResponseDto::new);

        return listResponseDto;
    }

    private boolean isSeller(String writer, String password, Item item) {
        if (!item.getWriter().equals(writer) ||
            !item.getPassword().equals(password)) {
            return false;
        }

        return true;
    }

    private boolean isBuyer(String writer, String password, Negotiation negotiation) {
        if (!negotiation.getWriter().equals(writer) ||
            !negotiation.getPassword().equals(password)) {
            return false;
        }

        return true;
    }

}
