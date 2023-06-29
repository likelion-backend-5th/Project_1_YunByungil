package com.example.market.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    private String status;
    private String writer;
    private String password;

    @Builder
    public Item(String title, String description, String imageUrl, int minPriceWanted, String writer, String password) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.minPriceWanted = minPriceWanted;
        this.status = "판매중";
        this.writer = writer;
        this.password = password;
    }
}
