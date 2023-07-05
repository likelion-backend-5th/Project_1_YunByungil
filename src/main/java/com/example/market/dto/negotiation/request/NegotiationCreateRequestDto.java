package com.example.market.dto.negotiation.request;

import com.example.market.domain.entity.Negotiation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NegotiationCreateRequestDto {

    @NotBlank
    private String writer;
    @NotBlank
    private String password;
    @PositiveOrZero
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
