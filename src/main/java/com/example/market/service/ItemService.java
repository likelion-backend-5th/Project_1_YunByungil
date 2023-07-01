package com.example.market.service;

import com.example.market.domain.entity.Item;
import com.example.market.dto.item.request.ItemCreateRequestDto;
import com.example.market.dto.item.request.ItemDeleteRequestDto;
import com.example.market.dto.item.request.ItemUpdateRequestDto;
import com.example.market.dto.item.response.ItemListResponseDto;
import com.example.market.dto.item.response.ItemOneResponseDto;
import com.example.market.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
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

    @Transactional
    public void updateItemImage(Long id, MultipartFile image, String writer, String password) throws IOException {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        checkWriterAndPassword(writer, password, item);

        // 파일 어디에 업로드? -> media/{userId}/profile.{파일 확장자}
        String profileDir = String.format("media/%d/", id);
        try {
            // 폴더만 만드는 과정
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 확장자를 포함한 이미지 이름 만들기 profile.{확장자}
        String originalFilename = image.getOriginalFilename();

        // img.jpg -> fileNameSplit = {"img", "jpg"}
        String[] fileNameSplit = originalFilename.split("\\.");

        String extension = fileNameSplit[fileNameSplit.length - 1];
        String profileFilename = "profile." +extension;

        // 폴더와 파일 경로를 포함한 이름 만들기
        String profilePath = profileDir + profileFilename;
        Path path = Path.of(profilePath);

        // MultipartFile 저장하기
        try {
            image.transferTo(path);
        } catch (IOException e) {
            e.getMessage();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // itemUpdate
        item.updateItemImage(String.format("/static/%d/%s", id, profileFilename));



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
