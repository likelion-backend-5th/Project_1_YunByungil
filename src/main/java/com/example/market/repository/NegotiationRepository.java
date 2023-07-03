package com.example.market.repository;

import com.example.market.domain.entity.Negotiation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {

    Page<Negotiation> findAllByItemId(Long itemId, Pageable pageable);
}
