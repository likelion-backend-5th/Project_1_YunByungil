package com.example.market.repository.chat;

import com.example.market.domain.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
