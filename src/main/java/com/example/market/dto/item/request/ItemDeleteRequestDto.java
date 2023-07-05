package com.example.market.dto.item.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemDeleteRequestDto {

    @NotBlank
    private String writer;
    @NotBlank
    private String password;

    @Builder
    public ItemDeleteRequestDto(String writer, String password) {
        this.writer = writer;
        this.password = password;
    }
}
