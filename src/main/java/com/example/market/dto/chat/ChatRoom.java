package com.example.market.dto.chat;

import com.example.market.domain.entity.chat.ChatRoomEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    private Long id;
    private String roomName;

    public static ChatRoom fromEntity(ChatRoomEntity entity) {
        return new ChatRoom(entity.getId(), entity.getRoomName());
    }
}
