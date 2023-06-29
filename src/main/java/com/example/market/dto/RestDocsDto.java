package com.example.market.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RestDocsDto {

    private String content;

    public RestDocsDto(String content) {
        this.content = content;
    }
}
