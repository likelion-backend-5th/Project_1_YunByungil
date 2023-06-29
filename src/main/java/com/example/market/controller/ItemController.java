package com.example.market.controller;

import com.example.market.dto.ItemCreateRequestDto;
import com.example.market.dto.ItemListResponseDto;
import com.example.market.dto.ItemOneResponseDto;
import com.example.market.dto.ItemResponseDto;
import com.example.market.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/items")
    public ResponseEntity<ItemResponseDto> create(@Valid @RequestBody ItemCreateRequestDto dto) {
        itemService.create(dto);

        ItemResponseDto responseDto = new ItemResponseDto("등록이 완료되었습니다.");
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/items")
    public Page<ItemListResponseDto> readItemList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "limit", defaultValue = "20") Integer limit) {

        Page<ItemListResponseDto> itemListResponseDto = itemService.readItemList(page, limit);

        return itemListResponseDto;
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemOneResponseDto> readItemOne(@PathVariable Long itemId) {
        ItemOneResponseDto itemDto = itemService.readItemOne(itemId);

        return ResponseEntity.ok().body(itemDto);
    }
}
