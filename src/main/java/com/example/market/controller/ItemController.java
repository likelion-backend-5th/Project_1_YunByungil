package com.example.market.controller;

import com.example.market.dto.item.request.ItemCreateRequestDto;
import com.example.market.dto.item.request.ItemDeleteRequestDto;
import com.example.market.dto.item.request.ItemUpdateRequestDto;
import com.example.market.dto.item.response.ItemListResponseDto;
import com.example.market.dto.item.response.ItemOneResponseDto;
import com.example.market.dto.item.response.ItemResponseDto;
import com.example.market.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
                                      @Valid @RequestBody ItemUpdateRequestDto dto) {
        itemService.updateItem(itemId, dto);

        return new ItemResponseDto("물품이 수정되었습니다.");
    }

    @DeleteMapping("/items/{itemId}")
    public ItemResponseDto deleteItem(@PathVariable Long itemId,
                                      @Valid @RequestBody ItemDeleteRequestDto dto) {
        itemService.deleteItem(itemId, dto);
        return new ItemResponseDto("물품을 삭제했습니다.");
    }

    @PutMapping("/items/{itemId}/image")
    public ItemResponseDto updateItemImage(@PathVariable Long itemId,
                                           @RequestParam MultipartFile image,
                                           @RequestParam String writer,
                                           @RequestParam String password) throws IOException {

        itemService.updateItemImage(itemId, image, writer, password);
        return new ItemResponseDto("이미지가 등록되었습니다.");
    }


}
