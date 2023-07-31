package com.example.market.viewController;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/views")
@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
