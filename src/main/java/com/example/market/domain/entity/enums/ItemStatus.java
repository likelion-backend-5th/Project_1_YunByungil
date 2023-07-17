package com.example.market.domain.entity.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ItemStatus {
    SALE("판매중"), SOLD("판매완료");
    private String status;
    ItemStatus(String status) {
        this.status = status;
    }
}
