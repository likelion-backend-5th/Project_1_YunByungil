package com.example.market.viewController;

import com.example.market.dto.chat.ChatRoom;
import com.example.market.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("rooms")
    public ResponseEntity<List<ChatRoom>> getChatRooms(){
        return ResponseEntity.ok(chatService.getChatRooms());
    }

    @PostMapping("rooms")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoom chatRoom,
                                               Authentication authentication){
        log.info("au!!! = {}", authentication.getName());
        return ResponseEntity.ok(chatService.createChatRoom(chatRoom));
    }

    @GetMapping("rooms/{id}/name")
    public ResponseEntity<ChatRoom> getRoomName(@PathVariable("id") Long roomId,
                                                Authentication authentication) {
        log.info("au!!! = {}", authentication.getName());
        return ResponseEntity.ok(chatService.findRoomById(roomId));
    }
}
