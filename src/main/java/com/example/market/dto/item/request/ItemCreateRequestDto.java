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
public class ItemCreateRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String description;
    @PositiveOrZero(message = "가격을 입력해주세요.")
    private int minPriceWanted;
    @NotBlank(message = "작성자를 입력해주세요.")
    private String writer;
    @NotBlank(message = "비밀번호를 입력해주세요.")
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
