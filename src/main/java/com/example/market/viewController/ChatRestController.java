package com.example.market.viewController;

import com.example.market.domain.entity.chat.ChatRoom;
import com.example.market.dto.chat.request.ChatRoomCreateDto;
import com.example.market.dto.chat.request.ChatSenderDto;
import com.example.market.dto.chat.response.ChatRoomCreateResponseDto;
import com.example.market.dto.chat.response.ChatRoomListResponseDto;
import com.example.market.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomListResponseDto>> getChatRooms(Authentication authentication){
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(chatService.getChatRooms(userId));
    }

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomCreateResponseDto> createRoom(@RequestBody ChatRoomCreateDto createDto,
                                                                Authentication authentication){
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(chatService.createChatRoom(createDto, userId));
    }

    @GetMapping("/username")
    public ResponseEntity<ChatSenderDto> getRoomName(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        ChatSenderDto writer = chatService.findRoomById(userId);
        return ResponseEntity.ok(writer);
    }
}
