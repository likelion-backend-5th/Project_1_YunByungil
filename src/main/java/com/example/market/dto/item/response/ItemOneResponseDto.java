package com.example.market.dto.item.response;

import com.example.market.domain.entity.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemOneResponseDto {

    private String title;
    private String description;
    private int minPriceWanted;
    private String status;

    public ItemOneResponseDto(Item item) {
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.minPriceWanted = item.getMinPriceWanted();
        this.status = item.getStatus();
    }
}
