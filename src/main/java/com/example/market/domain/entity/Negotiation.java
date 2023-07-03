package com.example.market.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Negotiation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;
    private int suggestedPrice;
    private String status;
    private String writer;
    private String password;

    @Builder
    public Negotiation(Long itemId, int suggestedPrice, String status, String writer, String password) {
        this.itemId = itemId;
        this.suggestedPrice = suggestedPrice;
        this.status = "제안";
        this.writer = writer;
        this.password = password;
    }
}
