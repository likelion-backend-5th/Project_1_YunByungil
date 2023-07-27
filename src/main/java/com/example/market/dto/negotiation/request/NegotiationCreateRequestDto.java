package com.example.market.dto.negotiation.request;

import com.example.market.domain.entity.Item;
import com.example.market.domain.entity.Negotiation;
import com.example.market.domain.entity.user.User;
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

    @PositiveOrZero
    private int suggestedPrice;

    @Builder
    public NegotiationCreateRequestDto(int suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public Negotiation toEntity(Item item, User user) {
        return Negotiation.builder()
                .item(item)
                .user(user)
                .suggestedPrice(suggestedPrice)
                .build();
    }
}
