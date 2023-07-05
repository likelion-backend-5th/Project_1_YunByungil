package com.example.market.dto.negotiation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NegotiationUpdateRequestDto {

    @NotBlank
    private String writer;
    @NotBlank
    private String password;
    @PositiveOrZero
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
