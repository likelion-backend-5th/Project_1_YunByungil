package com.example.market.dto;

import com.example.market.domain.entity.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemUpdateRequestDto {

    private String title;
    private String description;
    private int minPriceWanted;
    private String writer;
    private String password;

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
