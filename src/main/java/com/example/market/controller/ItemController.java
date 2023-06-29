package com.example.market.controller;

import com.example.market.dto.ItemCreateRequestDto;
import com.example.market.dto.ItemResponseDto;
import com.example.market.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/items")
    public ResponseEntity<ItemResponseDto> create(@RequestBody ItemCreateRequestDto dto) {
        itemService.create(dto);

        ItemResponseDto responseDto = new ItemResponseDto("등록이 완료되었습니다.");
        return ResponseEntity.ok().body(responseDto);
    }
}
