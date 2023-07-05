package com.example.market.controller;

import com.example.market.dto.negotiation.request.NegotiationCreateRequestDto;
import com.example.market.dto.negotiation.request.NegotiationDeleteRequestDto;
import com.example.market.dto.negotiation.request.NegotiationListRequestDto;
import com.example.market.dto.negotiation.request.NegotiationUpdateRequestDto;
import com.example.market.dto.negotiation.response.NegotiationListResponseDto;
import com.example.market.dto.negotiation.response.NegotiationResponseDto;
import com.example.market.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NegotiationController {

    private final NegotiationService negotiationService;

    @PostMapping("/items/{itemId}/proposals")
    public NegotiationResponseDto createNegotiation(@PathVariable Long itemId,
                                                    @Valid  @RequestBody NegotiationCreateRequestDto createDto) {
        negotiationService.createNegotiation(itemId, createDto);

        return new NegotiationResponseDto("구매 제안이 등록되었습니다.");
    }

    @GetMapping("/items/{itemId}/proposals")
    public Page<NegotiationListResponseDto> readAllNegotiation(@PathVariable Long itemId,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "limit", defaultValue = "20") int limit,
                                                               @Valid @RequestBody NegotiationListRequestDto listDto) {
        return negotiationService.readAllNegotiation(itemId, page, limit, listDto);
    }

    @PutMapping("/items/{itemId}/proposals/{proposalId}")
    public NegotiationResponseDto updateNegotiation(@PathVariable Long itemId,
                                                    @PathVariable Long proposalId,
                                                    @Valid @RequestBody NegotiationUpdateRequestDto updateDto) {
        return negotiationService.updateNegotiation(itemId, proposalId, updateDto);

    }

    @DeleteMapping("/items/{itemId}/proposals/{proposalId}")
    public NegotiationResponseDto deleteNegotiation(@PathVariable Long itemId,
                                                    @PathVariable Long proposalId,
                                                    @Valid @RequestBody NegotiationDeleteRequestDto deleteDto) {
        negotiationService.deleteNegotiation(itemId, proposalId, deleteDto);

        return new NegotiationResponseDto("제안을 삭제했습니다.");
    }
}
