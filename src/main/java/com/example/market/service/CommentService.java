package com.example.market.service;

import com.example.market.dto.comment.request.CommentCreateRequestDto;
import com.example.market.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public void create(Long itemId, CommentCreateRequestDto dto) {
        commentRepository.save(dto.toEntity(itemId));
    }
}
