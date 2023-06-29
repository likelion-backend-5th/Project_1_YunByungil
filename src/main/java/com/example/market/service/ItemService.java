package com.example.market.service;

import com.example.market.dto.ItemCreateRequestDto;
import com.example.market.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public void create(ItemCreateRequestDto dto) {
        itemRepository.save(dto.toEntity());
    }

}
