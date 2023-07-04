package com.example.market.repository;

import com.example.market.domain.entity.Negotiation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {

    boolean existsByItemId(Long itemId);

    Page<Negotiation> findAllByItemId(Long itemId, Pageable pageable);

    Page<Negotiation> findAllByItemIdAndWriterAndPassword(Long itemId, String writer, String password, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Negotiation n set n.status = '거절' where n.id <> :negotiationId and n.itemId = :itemId")
    int updateNegotiationStatus(@Param("negotiationId") Long negotiationId, @Param("itemId") Long itemId);
}
