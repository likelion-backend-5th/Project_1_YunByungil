package com.example.market.repository;

import com.example.market.domain.entity.Negotiation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NegotiationRepositoryTest {

    @Autowired
    NegotiationRepository repository;

    @DisplayName("Negotiation Repository 생성 및 조회 테스트")
    @Test
    void createAndFind() {
        // given
        final String defaultStatus = "제안";
        repository.save(Negotiation.builder()
                .writer("작성자")
                .suggestedPrice(10_000)
                .itemId(1L)
                .password("비밀번호")
                .build());

        // when
        List<Negotiation> all = repository.findAll();
        Negotiation negotiation = all.get(0);

        // then
        assertThat(negotiation.getItemId()).isEqualTo(1L);
        assertThat(negotiation.getStatus()).isEqualTo(defaultStatus);
    }
}