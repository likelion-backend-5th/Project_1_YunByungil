package com.example.market.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MarketAppException extends RuntimeException {
    private ErrorCode errorCode;

    public MarketAppException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
