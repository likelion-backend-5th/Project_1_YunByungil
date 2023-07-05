package com.example.market.dto.negotiation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NegotiationDeleteRequestDto {

    @NotBlank
    private String writer;
    @NotBlank
    private String password;

    @Builder
    public NegotiationDeleteRequestDto(String writer, String password) {
        this.writer = writer;
        this.password = password;
    }
}
