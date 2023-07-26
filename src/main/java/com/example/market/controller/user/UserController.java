package com.example.market.controller.user;

import com.example.market.dto.user.request.UserCreateRequestDto;
import com.example.market.dto.user.response.UserCreateResponseDto;
import com.example.market.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public UserCreateResponseDto createUser(@RequestBody UserCreateRequestDto createDto) {
        return userService.createUser(createDto);
    }
}
