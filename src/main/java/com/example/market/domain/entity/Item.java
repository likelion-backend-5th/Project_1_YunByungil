package com.example.market.domain.entity;

import com.example.market.domain.entity.enums.ItemStatus;
import com.example.market.domain.entity.user.User;
import com.example.market.dto.item.request.ItemUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "sales_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private int minPriceWanted;
    @Enumerated(EnumType.STRING)
    private ItemStatus status;
    private String writer;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Item(String title, String description, String imageUrl, int minPriceWanted, String writer, String password) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.minPriceWanted = minPriceWanted;
        this.status = ItemStatus.SALE;
        this.writer = writer;
        this.password = password;
    }

    public void update(ItemUpdateRequestDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.minPriceWanted = dto.getMinPriceWanted();
        this.writer = dto.getWriter();
        this.password = dto.getPassword();
    }

    public void updateItemImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateStatus(ItemStatus status) {
        this.status = status;
    }
}
