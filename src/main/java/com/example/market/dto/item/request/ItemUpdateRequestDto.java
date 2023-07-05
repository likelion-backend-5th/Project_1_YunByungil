package com.example.market.dto.item.request;

import com.example.market.domain.entity.Item;
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
    @NotBlank
    private String writer;
    @NotBlank
    private String password;

    @Builder
    public ItemUpdateRequestDto(String title, String description, int minPriceWanted, String writer, String password) {
        this.title = title;
        this.description = description;
        this.minPriceWanted = minPriceWanted;
        this.writer = writer;
        this.password = password;
    }

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .description(description)
                .minPriceWanted(minPriceWanted)
                .writer(writer)
                .password(password)
                .build();
    }
}
