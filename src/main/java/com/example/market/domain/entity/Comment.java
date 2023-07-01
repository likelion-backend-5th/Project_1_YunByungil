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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long item_id;
    private String writer;
    private String password;
    private String content;
    private String reply;

    @Builder
    public Comment(Long item_id, String writer, String password, String content, String reply) {
        this.item_id = item_id;
        this.writer = writer;
        this.password = password;
        this.content = content;
        this.reply = reply;
    }
}
