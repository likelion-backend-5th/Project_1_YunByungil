package com.example.market.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/chat")
@Controller
public class ChatController {

    @GetMapping
    public String index() {
        return "chat-lobby";
    }

    @GetMapping("/{roomId}/{userId}")
    public String enterRoom() {
        return "chat-room";
    }
}
