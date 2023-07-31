package com.example.market.viewController;

import com.example.market.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {

    private final TokenProvider tokenProvider;

    @GetMapping("/val")
    public Claims val(@RequestParam("token") String jwt) {
        return tokenProvider.getClaims(jwt);
    }
}
