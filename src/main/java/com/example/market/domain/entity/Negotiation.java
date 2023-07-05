package com.example.market.domain.entity;

import com.example.market.domain.entity.enums.NegotiationStatus;
import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private NegotiationStatus status;
    private String writer;
    private String password;

    @Builder
    public Negotiation(Long itemId, int suggestedPrice, String writer, String password) {
        this.itemId = itemId;
        this.suggestedPrice = suggestedPrice;
        this.status = NegotiationStatus.SUGGEST;
        this.writer = writer;
        this.password = password;
    }

    public void updateNegotiation(int suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public void updateNegotiationStatus(NegotiationStatus status) {
        this.status = status;
    }
}
