package com.example.market.service;

import com.example.market.domain.entity.chat.ChatRoomEntity;
import com.example.market.dto.chat.ChatRoom;
import com.example.market.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> getChatRooms() {
        List<ChatRoom> chatRoomList = new ArrayList<>();
        for (ChatRoomEntity chatRoomEntity : chatRoomRepository.findAll()) {
            chatRoomList.add(ChatRoom.fromEntity(chatRoomEntity));
        }

        return chatRoomList;
    }

    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.setRoomName(chatRoom.getRoomName());

        return ChatRoom.fromEntity(chatRoomRepository.save(chatRoomEntity));
    }

    public ChatRoom findRoomById(Long id) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(id).get();

        return ChatRoom.fromEntity(chatRoomEntity);
    }
}
