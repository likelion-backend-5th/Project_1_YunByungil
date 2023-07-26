package com.example.market.repository.user;

import com.example.market.domain.entity.enums.Role;
import com.example.market.domain.entity.user.Address;
import com.example.market.domain.entity.user.User;
import com.example.market.dto.user.request.UserCreateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @AfterEach
    void end() {
        userRepository.deleteAll();
    }

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
    @DisplayName("findByUsername() 메서드 테스트")
    @Test
    void findByUsername() {
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

        userRepository.save(createDto.toEntity(createDto.getPassword()));

        // when
        User savedUser = userRepository.findByUsername("아이디").get();

        // then
        assertThat(savedUser.getNickname()).isEqualTo("닉네임");
        assertThat(savedUser.getAddress().getCity()).isEqualTo(address.getCity());
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);
    }
}