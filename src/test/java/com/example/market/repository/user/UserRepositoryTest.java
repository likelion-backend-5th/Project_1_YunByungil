package com.example.market.repository.user;

import com.example.market.domain.entity.enums.Role;
import com.example.market.domain.entity.user.Address;
import com.example.market.domain.entity.user.User;
import com.example.market.dto.user.UserCreateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @DisplayName("회원정보 저장 테스트")
    @Test
    void createUser() {
        // given
        Address address = new Address("city", "street", "zipcode");
        UserCreateRequestDto createDto = UserCreateRequestDto.builder()
                .username("아이디")
                .password("비밀번호")
                .email("이메일")
                .nickname("닉네임")
                .phoneNumber("번호")
                .userImage("사진")
                .address(address)
                .build();

        // when
        User savedUser = userRepository.save(createDto.toEntity(createDto.getPassword()));

        // then
        assertThat(savedUser.getNickname()).isEqualTo("닉네임");
        assertThat(savedUser.getAddress().getCity()).isEqualTo(address.getCity());
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);
    }
}