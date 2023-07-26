package com.example.market.dto.user.response;

import com.example.market.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCreateResponseDto {
    private Long id;
    private String username;

    public UserCreateResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
