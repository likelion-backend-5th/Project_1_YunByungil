package com.example.market.dto.negotiation.request;

import com.example.market.domain.entity.Negotiation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NegotiationCreateRequestDto {

    private String writer;
    private String password;
    private int suggestedPrice;

    @Builder
    public NegotiationCreateRequestDto(String writer, String password, int suggestedPrice) {
        this.writer = writer;
        this.password = password;
        this.suggestedPrice = suggestedPrice;
    }

    public Negotiation toEntity(Long itemId) {
        return Negotiation.builder()
                .writer(writer)
                .password(password)
                .suggestedPrice(suggestedPrice)
                .itemId(itemId)
                .build();
    }
}
