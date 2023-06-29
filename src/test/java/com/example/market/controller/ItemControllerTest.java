package com.example.market.controller;

import com.example.market.domain.entity.Item;
import com.example.market.dto.ItemCreateRequestDto;
import com.example.market.repository.ItemRepository;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest
class ItemControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        itemRepository.deleteAll();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint()))
                .build();
    }

    @AfterEach
    void end() {
        itemRepository.deleteAll();
    }

    private ItemCreateRequestDto createRequestDto() {
        ItemCreateRequestDto dto = ItemCreateRequestDto.builder()
                .title("제목1")
                .description("내용1")
                .minPriceWanted(10_000)
                .writer("작성자1")
                .password("비밀번호1")
                .build();

        return dto;
    }

    @DisplayName("아이템 생성 API 테스트")
    @Test
    void createItem() throws Exception {

        // given
        String url = "https://localhost:8080/items";

        ItemCreateRequestDto dto = createRequestDto();

        // when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("/items",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("description").description("내용"),
                                fieldWithPath("minPriceWanted").description("최소가격"),
                                fieldWithPath("writer").description("작성자"),
                                fieldWithPath("password").description("비밀번호")

                        )
                ));

        // then
    }

    @DisplayName("아이템 단일 조회 API")
    @Test
    void readItemOne() throws Exception {
        // given
        String url = "https://localhost:8080/items/";

        ItemCreateRequestDto dto = createRequestDto();
        Item savedItem = itemRepository.save(dto.toEntity());

        // when
        mvc.perform(get(url + "{id}", savedItem.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()))
                .andDo(document("/items-get-one",
                        responseFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("description").description("내용"),
                                fieldWithPath("minPriceWanted").description("가격"),
                                fieldWithPath("status").description("판매여부")
                        ),
                        pathParameters(
                                parameterWithName("id").description("Item 번호")
                        )));

        // then

    }


}