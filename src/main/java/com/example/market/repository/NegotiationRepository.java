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

    @Query("select n " +
            "from Negotiation n " +
            "join fetch n.item " +
            "where n.item.id =:itemId")
    Page<Negotiation> findAllByItemId(@Param("itemId") Long itemId, Pageable pageable);

    @Query("select n " +
            "from Negotiation n " +
            "join fetch n.item " +
            "join fetch n.user " +
            "where n.item.id =:itemId " +
            "and n.user.id =:userId")
    Page<Negotiation> findAllByItemIdAndUserId(@Param("itemId") Long itemId, @Param("userId") Long userId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Negotiation n set n.status = 'REJECT' where n.id <> :negotiationId and n.item.id = :itemId")
    int updateNegotiationStatus(@Param("negotiationId") Long negotiationId, @Param("itemId") Long itemId);
}
