package com.example.market.repository;

import com.example.market.domain.entity.Negotiation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
}
