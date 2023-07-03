package com.example.market.controller;

import com.example.market.dto.negotiation.request.NegotiationCreateRequestDto;
import com.example.market.dto.negotiation.response.NegotiationResponseDto;
import com.example.market.service.NegotiationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NegotiationController {

    private final NegotiationService negotiationService;

    @PostMapping("/items/{itemId}/proposal")
    public NegotiationResponseDto createNegotiation(@PathVariable Long itemId,
                                                    @RequestBody NegotiationCreateRequestDto createDto) {
        negotiationService.createNegotiation(itemId, createDto);

        return new NegotiationResponseDto("구매 제안이 등록되었습니다.");
    }
}
