package com.example.market.viewController;

import com.example.market.dto.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WebSocketMapping {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void sendChat(ChatMessage chatMessage,
                         @Headers Map<String, Object> headers,
                         @Header("nativeHeaders") Map<String, String> nativeHeaders) {
        log.info(chatMessage.toString());
        log.info(headers.toString());
        log.info(nativeHeaders.toString());

        String time = new SimpleDateFormat("HH:mm").format(new Date());

        chatMessage.setTime(time);
        simpMessagingTemplate.convertAndSend(
                String.format("/topic/%s", chatMessage.getRoomId()),
                chatMessage
        );
    }

    @SubscribeMapping("/topic/{roomId}") // 입장할 때 누가 입장했는지(누가 연결되었는지)
    public ChatMessage sendGreet(@DestinationVariable("roomId") Long roomId) {
        log.info("new subscription to {}", roomId);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setSender("admin");
        chatMessage.setMessage("hello!");
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        chatMessage.setTime(time);

        return chatMessage;
    }

}
