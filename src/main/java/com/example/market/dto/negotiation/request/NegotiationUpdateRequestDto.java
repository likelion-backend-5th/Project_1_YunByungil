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
    private String status;

    @Builder
    public NegotiationUpdateRequestDto(String writer, String password, int suggestedPrice, String status) {
        this.writer = writer;
        this.password = password;
        this.suggestedPrice = suggestedPrice;
        this.status = status;
    }
}
