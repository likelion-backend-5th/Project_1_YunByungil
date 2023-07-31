package com.example.market.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/views")
@Controller
public class UserViewController {

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }
}
