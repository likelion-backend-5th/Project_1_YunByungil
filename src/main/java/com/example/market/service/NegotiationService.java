package com.example.market.service;

import com.example.market.domain.entity.Item;
import com.example.market.domain.entity.Negotiation;
import com.example.market.domain.entity.enums.ItemStatus;
import com.example.market.domain.entity.enums.NegotiationStatus;
import com.example.market.domain.entity.user.User;
import com.example.market.dto.negotiation.request.*;
import com.example.market.dto.negotiation.response.NegotiationListResponseDto;
import com.example.market.dto.negotiation.response.NegotiationResponseDto;
import com.example.market.exception.MarketAppException;
import com.example.market.repository.ItemRepository;
import com.example.market.repository.NegotiationRepository;
import com.example.market.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.example.market.common.SystemMessage.*;
import static com.example.market.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NegotiationService {

    private final NegotiationRepository negotiationRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createNegotiation(Long itemId, NegotiationCreateRequestDto dto, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new MarketAppException(NOT_FOUND_ITEM, NOT_FOUND_ITEM.getMessage()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return negotiationRepository.save(dto.toEntity(item, user)).getId();
    }

    public Page<NegotiationListResponseDto> readAllNegotiation(Long itemId, int page, int limit, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new MarketAppException(NOT_FOUND_ITEM, NOT_FOUND_ITEM.getMessage()));

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 1. 아이템 주인일 때 그 아이템에 대한 네고 정보를 다 불러와야 함.
        if (item.getUser().getId() == user.getId()) {
            return getNegotiationListResponseDtoBySeller(itemId, pageable);
        }

        // 2. 주인이 아니기 때문에 Buyer 기준
        return getNegotiationListResponseDtoByBuyer(itemId, userId, pageable);
    }

    @Transactional
    public NegotiationResponseDto updateNegotiation(Long itemId, Long negotiationId, NegotiationUpdateRequestDto updateDto, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new MarketAppException(NOT_FOUND_ITEM, NOT_FOUND_ITEM.getMessage()));

        Negotiation negotiation = negotiationRepository.findById(negotiationId)
                .orElseThrow(() -> new MarketAppException(NOT_FOUND_NEGOTIATION, NOT_FOUND_NEGOTIATION.getMessage()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        NegotiationStatus status = null;
        if (updateDto.getSuggestedPrice() == 0) {
            status = NegotiationStatus.findByNegotiationStatus(updateDto.getStatus());
        }

        if (item.getUser().getId() == user.getId()) {
            negotiation.updateNegotiationStatus(status);
            return new NegotiationResponseDto(CHANGE_SUGGEST_STATUS);
        }

        if (negotiation.getUser().getId() == user.getId()) {
            if (hasSuggestedPrice(updateDto)) {
                reviseSuggestedPrice(updateDto, negotiation);
                return new NegotiationResponseDto(CHANGE_SUGGEST);
            }

            if (hasStatus(updateDto)) {
                if (isStatusAccept(negotiation)) {
                    changeProposalToAccept(item, negotiation, status);
                    changeProposalToReject(itemId, negotiationId);
                    return new NegotiationResponseDto(PURCHASE_CONFIRM);
                }
            }
        }

        // TODO: refactor
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public void deleteNegotiation(Long itemId, Long negotiationId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new MarketAppException(NOT_FOUND_ITEM, NOT_FOUND_ITEM.getMessage()));

        Negotiation negotiation = negotiationRepository.findById(negotiationId)
                .orElseThrow(() -> new MarketAppException(NOT_FOUND_NEGOTIATION, NOT_FOUND_NEGOTIATION.getMessage()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (negotiation.getUser().getId() != user.getId()) {
            throw new MarketAppException(INVALID_WRITER, INVALID_WRITER.getMessage());
        }

        negotiationRepository.delete(negotiation);
    }

    private boolean isStatusAccept(Negotiation negotiation) {
        return negotiation.getStatus().equals(NegotiationStatus.ACCEPT);
    }

    private boolean hasStatus(NegotiationUpdateRequestDto updateDto) {
        return updateDto.getSuggestedPrice() == 0;
    }

    private boolean hasSuggestedPrice(NegotiationUpdateRequestDto updateDto) {
        return updateDto.getSuggestedPrice() != 0 && updateDto.getStatus() == null;
    }

    private void changeProposalToAccept(Item item, Negotiation negotiation, NegotiationStatus status) {
        negotiation.updateNegotiationStatus(status);
        item.updateStatus(ItemStatus.SOLD);
    }

    private int changeProposalToReject(Long itemId, Long negotiationId) {
        return negotiationRepository.updateNegotiationStatus(negotiationId, itemId);
    }

    private void reviseSuggestedPrice(NegotiationUpdateRequestDto updateDto, Negotiation negotiation) {
        negotiation.updateNegotiation(updateDto.getSuggestedPrice());
    }

    private Page<NegotiationListResponseDto> getNegotiationListResponseDtoBySeller(Long itemId, Pageable pageable) {
        Page<Negotiation> allByItemId = negotiationRepository.findAllByItemId(itemId, pageable);

        Page<NegotiationListResponseDto> listResponseDto = allByItemId.map(NegotiationListResponseDto::new);

        return listResponseDto;
    }

    private Page<NegotiationListResponseDto> getNegotiationListResponseDtoByBuyer(Long itemId, Long userId, Pageable pageable) {
        Page<Negotiation> allByItemIdAndUserId = negotiationRepository.findAllByItemIdAndUserId(itemId, userId, pageable);

        Page<NegotiationListResponseDto> listResponseDto = allByItemIdAndUserId.map(NegotiationListResponseDto::new);

        return listResponseDto;
    }
}
