package com.example.market.dto.negotiation.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NegotiationListRequestDto {

    private String writer;
    private String password;

    @Builder
    public NegotiationListRequestDto(String writer, String password) {
        this.writer = writer;
        this.password = password;
    }
}
