package com.example.market.repository;

import com.example.market.domain.entity.Item;
import com.example.market.dto.item.request.ItemCreateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @DisplayName("itemRepository.save() 기능 테스트")
    @Test
    void saveItem() {
        // given
        ItemCreateRequestDto dto = ItemCreateRequestDto.builder()
                .title("제목1")
                .description("내용1")
                .minPriceWanted(10_000)
                .writer("작성자1")
                .password("비밀번호1")
                .build();

        // when
        Item item = itemRepository.save(dto.toEntity());

        // then
        assertThat(item.getTitle()).isEqualTo(dto.getTitle());

    }
}