package com.example.market.service;

import com.example.market.domain.entity.Item;
import com.example.market.dto.negotiation.request.NegotiationCreateRequestDto;
import com.example.market.repository.ItemRepository;
import com.example.market.repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
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
}
