package com.example.market.service;

import com.example.market.domain.entity.Item;
import com.example.market.dto.ItemCreateRequestDto;
import com.example.market.dto.ItemListResponseDto;
import com.example.market.dto.ItemOneResponseDto;
import com.example.market.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @AfterEach
    void end() {
        itemRepository.deleteAll();
    }

    @DisplayName("아이템 등록 테스트")
    @Test
    void createItem() {

        // given
        ItemCreateRequestDto dto = ItemCreateRequestDto.builder()
                .title("제목1")
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

    @DisplayName("아이템 전체 조회(페이징) 테스트")
    @Test
    void readItemList() {
        // given
        Integer page = 0;
        Integer limit = 20;
        createItems();

        // when
        Page<ItemListResponseDto> itemListDto = itemService.readItemList(page, limit);

        // then
        assertThat(itemListDto.getSize()).isEqualTo(20);

    }

    @DisplayName("아이템 단일 조회 테스트")
    @Test
    void readItemOne() {
        // given
        ItemCreateRequestDto dto = ItemCreateRequestDto.builder()
                .title("제목1")
                .description("내용1")
                .minPriceWanted(10_000)
                .writer("작성자1")
                .password("비밀번호1")
                .build();

        Item save = itemRepository.save(dto.toEntity());

        // when
        ItemOneResponseDto itemOneResponseDto = itemService.readItemOne(save.getId());

        // then
        assertThat(itemOneResponseDto.getTitle()).isEqualTo(dto.getTitle());
    }

    @DisplayName("존재하지 않는 아이템일 때 예외 발생")
    @Test
    void notItemException() {
        assertThatThrownBy(() -> {
            itemService.readItemOne(1L);
        }).isInstanceOf(ResponseStatusException.class);
    }

    private void createItems() {
        for (int i = 1; i <= 30; i++) {
            ItemCreateRequestDto dto = ItemCreateRequestDto.builder()
                    .title("제목" + i)
                    .description("내용" + i)
                    .minPriceWanted(i)
                    .writer("작성자" + i)
                    .password("비밀번호" + i)
                    .build();

            itemService.create(dto);
        }
    }

}