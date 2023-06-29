package com.example.market.dto;

import com.example.market.domain.entity.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemCreateRequestDto {

    private String title;
    private String description;
    private int minPriceWanted;
    private String writer;
    private String password;

    @Builder
    public ItemCreateRequestDto(String title, String description, int minPriceWanted, String writer, String password) {
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
