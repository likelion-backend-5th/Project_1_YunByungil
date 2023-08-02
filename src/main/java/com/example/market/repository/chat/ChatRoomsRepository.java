package com.example.market.repository.chat;

import com.example.market.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomsRepository extends JpaRepository<ChatRoom, Long> {
}
