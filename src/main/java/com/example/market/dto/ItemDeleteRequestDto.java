package com.example.market.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemDeleteRequestDto {

    private String writer;
    private String password;

    @Builder
    public ItemDeleteRequestDto(String writer, String password) {
        this.writer = writer;
        this.password = password;
    }
}
