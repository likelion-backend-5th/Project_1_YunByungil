package com.example.market.dto.item.request;

import com.example.market.domain.entity.Item;
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
public class ItemUpdateRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @PositiveOrZero
    private int minPriceWanted;

    @Builder
    public ItemUpdateRequestDto(String title, String description, int minPriceWanted) {
        this.title = title;
        this.description = description;
        this.minPriceWanted = minPriceWanted;
    }

    public Item toEntity(User user) {
        return Item.builder()
                .title(title)
                .description(description)
                .minPriceWanted(minPriceWanted)
                .user(user)
                .build();
    }
}
