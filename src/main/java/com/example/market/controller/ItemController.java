package com.example.market.controller;

import com.example.market.dto.*;
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

    @PutMapping("/items/{itemId}")
    public ItemResponseDto updateItem(@PathVariable Long itemId,
                                      @RequestBody ItemUpdateRequestDto dto) {
        itemService.updateItem(itemId, dto);

        return new ItemResponseDto("물품이 수정되었습니다.");
    }

    @DeleteMapping("/items/{itemId}")
    public ItemResponseDto deleteItem(@PathVariable Long itemId,
                                      @RequestBody ItemDeleteRequestDto dto) {
        itemService.deleteItem(itemId, dto);
        return new ItemResponseDto("물품을 삭제했습니다.");
    }
}
