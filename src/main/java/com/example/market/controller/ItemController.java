package com.example.market.controller;

import com.example.market.dto.item.request.ItemCreateRequestDto;
import com.example.market.dto.item.request.ItemUpdateRequestDto;
import com.example.market.dto.item.response.ItemListResponseDto;
import com.example.market.dto.item.response.ItemOneResponseDto;
import com.example.market.dto.item.response.ItemResponseDto;
import com.example.market.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.market.common.SystemMessage.*;

@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/items")
    public ResponseEntity<ItemResponseDto> create(@Valid @RequestBody ItemCreateRequestDto dto,
                                                  Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        itemService.create(dto, userId);

        ItemResponseDto responseDto = new ItemResponseDto(REGISTER_ITEM);
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
                                      @Valid @RequestBody ItemUpdateRequestDto dto,
                                      Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        itemService.updateItem(itemId, dto, userId);

        return new ItemResponseDto(UPDATE_ITEM);
    }

    @DeleteMapping("/items/{itemId}")
    public ItemResponseDto deleteItem(@PathVariable Long itemId,
                                      Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        itemService.deleteItem(itemId, userId);
        return new ItemResponseDto(DELETE_ITEM);
    }

    @PutMapping("/items/{itemId}/image")
    public ItemResponseDto updateItemImage(@PathVariable Long itemId,
                                           @RequestParam MultipartFile image,
                                           Authentication authentication) throws IOException {
        Long userId = Long.parseLong(authentication.getName());
        itemService.updateItemImage(itemId, image, userId);
        return new ItemResponseDto(REGISTER_IMAGE);
    }
}
