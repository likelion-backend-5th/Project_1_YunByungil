package com.example.market.dto;

import com.example.market.domain.entity.Item;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemCreateRequestDto {

    @NotNull(message = "제목을 입력해주세요.")
    private String title;
    @NotNull(message = "내용을 입력해주세요.")
    private String description;
    @NotNull(message = "가격을 입력해주세요.")
    private int minPriceWanted;
    @NotNull(message = "작성자를 입력해주세요.")
    private String writer;
    @NotNull(message = "비밀번호를 입력해주세요.")
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
