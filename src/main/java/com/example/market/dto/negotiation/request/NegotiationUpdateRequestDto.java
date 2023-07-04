package com.example.market.dto.negotiation.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NegotiationUpdateRequestDto {

    private String writer;
    private String password;
    private int suggestedPrice;

    @Builder
    public NegotiationUpdateRequestDto(String writer, String password, int suggestedPrice) {
        this.writer = writer;
        this.password = password;
        this.suggestedPrice = suggestedPrice;
    }
}
