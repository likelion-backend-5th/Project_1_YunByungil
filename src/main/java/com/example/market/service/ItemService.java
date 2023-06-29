package com.example.market.service;

import com.example.market.domain.entity.Item;
import com.example.market.dto.*;
import com.example.market.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
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

    @Transactional
    public void updateItem(Long id, ItemUpdateRequestDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        checkWriterAndPassword(dto.getWriter(), dto.getPassword(), item);

        item.update(dto);
    }

    @Transactional
    public void deleteItem(Long id, ItemDeleteRequestDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        checkWriterAndPassword(dto.getWriter(), dto.getPassword(), item);

        itemRepository.delete(item);
    }

    private void checkWriterAndPassword(String writer, String password , Item item) {
        if (!item.getWriter().equals(writer)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!item.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
