package com.example.market.domain.entity.user;

import com.example.market.domain.entity.Item;
import com.example.market.domain.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String nickname;
    @Embedded
    private Address address;
    private String userImage;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Item> items = new ArrayList<>();

    @Builder
    public User(String username, String password, String phoneNumber, String email, String nickname,
                Address address, String userImage, Role role) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.userImage = userImage;
        this.role = role;
    }
}
