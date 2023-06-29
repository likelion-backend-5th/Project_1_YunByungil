package com.example.market.service;

import com.example.market.domain.entity.Item;
import com.example.market.dto.ItemCreateRequestDto;
import com.example.market.dto.ItemListResponseDto;
import com.example.market.dto.ItemOneResponseDto;
import com.example.market.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public void create(ItemCreateRequestDto dto) {
        itemRepository.save(dto.toEntity());
    }

    public Page<ItemListResponseDto> readItemList(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<Item> itemList = itemRepository.findAll(pageable);

        Page<ItemListResponseDto> result = itemList.map(ItemListResponseDto::new);

        return result;
    }

    public ItemOneResponseDto readItemOne(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        return new ItemOneResponseDto(item);
    }

}
