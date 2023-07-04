package com.example.market.controller;

import com.example.market.domain.entity.Item;
import com.example.market.domain.entity.Negotiation;
import com.example.market.dto.negotiation.request.NegotiationCreateRequestDto;
import com.example.market.dto.negotiation.request.NegotiationDeleteRequestDto;
import com.example.market.dto.negotiation.request.NegotiationUpdateRequestDto;
import com.example.market.repository.CommentRepository;
import com.example.market.repository.ItemRepository;
import com.example.market.repository.NegotiationRepository;
import com.example.market.service.CommentService;
import com.example.market.service.NegotiationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest
class NegotiationControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;


    @Autowired
    NegotiationRepository negotiationRepository;

    @Autowired
    NegotiationService negotiationService;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withResponseDefaults(prettyPrint())
                        .withRequestDefaults(prettyPrint()))
                .build();
    }

    @AfterEach
    void end() {
        itemRepository.deleteAll();
        negotiationRepository.deleteAll();
    }

    @DisplayName("구매 제안 등록 API")
    @Test
    void createNegotiation() throws Exception {
        // given
        Long itemId = createItem();

        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .suggestedPrice(5_000)
                .build();

        String url = "http://localhost:8080/items/{itemId}/proposals";

        // when
        mvc.perform(post(url, itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(document("/proposals",
                        requestFields(
                                fieldWithPath("writer").description("제안 작성자"),
                                fieldWithPath("password").description("제안 비밀번호"),
                                fieldWithPath("suggestedPrice").description("제안 가격")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("물품 ID")
                        )));

        // then

    }

    @DisplayName("구매 제안 가격 수정 API")
    @Test
    void updateNegotiation() throws Exception {
        // given
        Long itemId = createItem();
        Long negotiationId = createNegotiationOne(itemId);

        NegotiationUpdateRequestDto updateDto = NegotiationUpdateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .suggestedPrice(20_000)
                .build();

        String url = "http://localhost:8080/items/{itemId}/proposals/{proposalId}";

        // when
        mvc.perform(put(url, itemId, negotiationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(document("/proposals-update",
                        requestFields(
                                fieldWithPath("writer").description("제안 작성자"),
                                fieldWithPath("password").description("제안 비밀번호"),
                                fieldWithPath("suggestedPrice").description("수정한 제안 가격"),
                                fieldWithPath("status").description("null")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("물품 ID"),
                                parameterWithName("proposalId").description("제안 ID")

                        )));

        // then

    }

    @DisplayName("구매 제안 삭제 API")
    @Test
    void deleteNegotiation() throws Exception {
        // given
        Long itemId = createItem();
        Long negotiationId = createNegotiationOne(itemId);

        NegotiationDeleteRequestDto deleteDto = NegotiationDeleteRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .build();

        String url = "http://localhost:8080/items/{itemId}/proposals/{proposalId}";

        // when
        mvc.perform(delete(url, itemId, negotiationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deleteDto)))
                .andExpect(status().isOk())
                .andDo(document("/proposals-delete",
                        requestFields(
                                fieldWithPath("writer").description("제안 작성자"),
                                fieldWithPath("password").description("제안 비밀번호")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("물품 ID"),
                                parameterWithName("proposalId").description("제안 ID")

                        )));

        // then

    }

    @DisplayName("구매 제안 상태 변경 API")
    @Test
    void updateNegotiationStatus() throws Exception {
        // given
        Long itemId = createItem();
        Long negotiationId = createNegotiationOne(itemId);

        NegotiationUpdateRequestDto updateDto = NegotiationUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .status("수락 || 거절")
                .build();

        String url = "http://localhost:8080/items/{itemId}/proposals/{proposalId}";

        // when
        mvc.perform(put(url, itemId, negotiationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(document("/proposals-statusUpdate",
                        requestFields(
                                fieldWithPath("writer").description("아이템 작성자"),
                                fieldWithPath("password").description("아이템 비밀번호"),
                                fieldWithPath("status").description("제안 상태 - 수락 or 거절"),
                                fieldWithPath("suggestedPrice").description("0")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("물품 ID"),
                                parameterWithName("proposalId").description("제안 ID")

                        )));

        // then

    }

    @DisplayName("구매 확정 API")
    @Test
    void purchaseConfirm() throws Exception {
        // given
        Long itemId = createItem();
        Long negotiationId = createNegotiationStatusIsAccept(itemId);



        NegotiationUpdateRequestDto updateDto = NegotiationUpdateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .status("확정")
                .build();


        String url = "http://localhost:8080/items/{itemId}/proposals/{proposalId}";

        // when
        mvc.perform(put(url, itemId, negotiationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(document("/proposals-purchaseConfirm",
                        requestFields(
                                fieldWithPath("writer").description("제안 작성자"),
                                fieldWithPath("password").description("제안 비밀번호"),
                                fieldWithPath("status").description("확정"),
                                fieldWithPath("suggestedPrice").description("0")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("물품 ID"),
                                parameterWithName("proposalId").description("제안 ID")

                        )));

        // then

    }



    private Long createItem() {
        Item item = itemRepository.save(Item.builder()
                .title("제목")
                .description("내용")
                .writer("작성자")
                .password("비밀번호")
                .minPriceWanted(10_000)
                .build());

        return item.getId();
    }

    private Long createNegotiationOne(Long itemId) {
        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .suggestedPrice(5_000)
                .build();

        Negotiation negotiation = negotiationRepository.save(createDto.toEntity(itemId));

        return negotiation.getId();
    }

    private Long createNegotiationStatusIsAccept(Long itemId) {
        NegotiationCreateRequestDto createDto = NegotiationCreateRequestDto.builder()
                .writer("제안 작성자")
                .password("제안 비밀번호")
                .suggestedPrice(5_000)
                .build();

        Negotiation negotiation = negotiationRepository.save(createDto.toEntity(itemId));

        NegotiationUpdateRequestDto updateStatusDto = NegotiationUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .status("수락")
                .build();

        negotiationService.updateNegotiation(itemId, negotiation.getId(), updateStatusDto);

        return negotiation.getId();
    }
}