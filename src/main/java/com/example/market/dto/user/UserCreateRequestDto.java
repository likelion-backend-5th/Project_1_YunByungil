package com.example.market.dto.user;

import com.example.market.domain.entity.enums.Role;
import com.example.market.domain.entity.user.Address;
import com.example.market.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCreateRequestDto {
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String nickname;
    private Address address;
    private String userImage;

    @Builder
    public UserCreateRequestDto(String username, String password, String phoneNumber, String email,
                                String nickname, Address address, String userImage) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.userImage = userImage;
    }

    public User toEntity(String password) {
        return User.builder()
                .username(username)
                .password(password)
                .phoneNumber(phoneNumber)
                .email(email)
                .nickname(nickname)
                .address(address)
                .userImage(userImage)
                .role(Role.USER)
                .build();
    }
}
