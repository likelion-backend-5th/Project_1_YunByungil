package com.example.market.service;

import com.example.market.domain.entity.Item;
import com.example.market.dto.ItemCreateRequestDto;
import com.example.market.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @DisplayName("아이템 등록 테스트")
    @Test
    void createItem() {

        // given
        ItemCreateRequestDto dto = ItemCreateRequestDto.builder()
                .description("내용1")
                .minPriceWanted(10_000)
                .writer("작성자1")
                .password("비밀번호1")
                .build();

        itemService.create(dto);
        // when
        List<Item> all = itemRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(1);

    }

}