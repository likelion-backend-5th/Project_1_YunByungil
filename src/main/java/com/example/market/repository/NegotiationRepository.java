package com.example.market.repository;

import com.example.market.domain.entity.Negotiation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {

    boolean existsByItemId(Long itemId);

    Page<Negotiation> findAllByItemId(Long itemId, Pageable pageable);

    Page<Negotiation> findAllByItemIdAndWriterAndPassword(Long itemId, String writer, String password, Pageable pageable);
}
