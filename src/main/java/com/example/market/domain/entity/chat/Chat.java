package com.example.market.domain.entity.chat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private Long senderId;

    private Long recipient;

    private String content;

    @Builder
    public Chat(ChatRoom chatRoom, Long senderId, Long recipientId, String content) {
        this.chatRoom = chatRoom;
        this.senderId = senderId;
        this.recipient = recipientId;
        this.content = content;
    }


}
