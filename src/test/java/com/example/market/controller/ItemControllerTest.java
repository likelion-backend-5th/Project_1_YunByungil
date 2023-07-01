package com.example.market.controller;

import com.example.market.domain.entity.Item;
import com.example.market.dto.ItemCreateRequestDto;
import com.example.market.dto.ItemDeleteRequestDto;
import com.example.market.dto.ItemUpdateRequestDto;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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

    @DisplayName("아이템 페이징 조회 API")
    @Test
    void readItemList() throws Exception {
        // given
        for (int i = 0; i < 1; i++) {
            itemRepository.save(Item.builder()
                    .title("제목" + i)
                    .description("내용" + i)
                    .minPriceWanted(i)
                    .writer("작성자" + i)
                    .password("비밀번호" + i)
                    .build());
        }

        Integer page = 0;
        Integer limit = 5;
        String url = "https://localhost:8080/items";

        // when
        mvc.perform(get(url)
                        .param("page", "0")
                        .param("limit", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].title").value("제목" + 0))
//                .andExpect(jsonPath("$[0].description").value("내용" + 0))
                .andDo(document("/items-get-all",
                        queryParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("한 페이지당 조회할 데이터 개수 (default = 20)")
                        ),
                        responseFields(
                                fieldWithPath("content.[].id").description("ID"),
                                fieldWithPath("content.[].title").description("제목"),
                                fieldWithPath("content.[].description").description("내용"),
                                fieldWithPath("content.[].minPriceWanted").description("최소가격"),
                                fieldWithPath("content.[].imageUrl").description("이미지"),
                                fieldWithPath("content.[].status").description("판매여부"),

                                fieldWithPath("pageable.sort.sorted").description("정렬 여부"),
                                fieldWithPath("pageable.sort.empty").description("데이터가 비었는지 여부"),
                                fieldWithPath("pageable.sort.unsorted").description("정렬 안 됐는지 여부"),

                                fieldWithPath("pageable.offset").description("몇 번째 데이터인지 (0부터 시작)"),
                                fieldWithPath("pageable.pageNumber").description("현재 페이지 번호"),
                                fieldWithPath("pageable.pageSize").description("한 페이지당 조회할 데이터 개수"),
                                fieldWithPath("pageable.paged").description("페이징 정보를 포함하는지 여부"),
                                fieldWithPath("pageable.unpaged").description("페이징 정보를 안 포함하는지 여부"),

                                fieldWithPath("last").description("마지막 페이지인지 여부"),
                                fieldWithPath("totalPages").description("전체 페이지 개수"),
                                fieldWithPath("totalElements").description("테이블 총 데이터 개수"),
                                fieldWithPath("first").description("첫번째 페이지인지 여부"),
                                fieldWithPath("numberOfElements").description("요청 페이지에서 조회 된 데이터 개수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("size").description("한 페이지당 조회할 데이터 개수"),

                                fieldWithPath("sort.sorted").description("정렬 됐는지 여부"),
                                fieldWithPath("sort.unsorted").description("정렬 안 됐는지 여부"),
                                fieldWithPath("sort.empty").description("데이터가 비었는지 여부"),

                                fieldWithPath("empty").description("데이터가 비었는지 여부")
                        )
                ));


        // then

    }

    @DisplayName("아이템 정보 수정 API 테스트")
    @Test
    void updateItemApi() throws Exception {
        // given
        ItemCreateRequestDto dto = createRequestDto();
        Item savedItem = itemRepository.save(dto.toEntity());

        ItemUpdateRequestDto updateDto = ItemUpdateRequestDto.builder()
                .title("수정 완료")
                .minPriceWanted(20_000)
                .description("수정 완료")
                .writer(dto.getWriter())
                .password(dto.getPassword())
                .build();

        String url = "http://localhost:8080/items/{itemId}";

        // when
        mvc.perform(put(url, savedItem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(document("/items-update",
                        pathParameters(
                                parameterWithName("itemId").description("ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("수정할 제목"),
                                fieldWithPath("description").description("수정할 내용"),
                                fieldWithPath("minPriceWanted").description("수정할 금액"),
                                fieldWithPath("writer").description("작성자 계정"),
                                fieldWithPath("password").description("작성자 비밀번호")
                        )));

        // then

    }

    @DisplayName("아이템 삭제 API 테스트")
    @Test
    void deleteItemApi() throws Exception {
        // given
        ItemCreateRequestDto dto = createRequestDto();
        Item savedItem = itemRepository.save(dto.toEntity());

        ItemDeleteRequestDto deleteDto = ItemDeleteRequestDto.builder()
                .writer(dto.getWriter())
                .password(dto.getPassword())
                .build();

        String url = "http://localhost:8080/items/{itemId}";
        // when
        mvc.perform(delete(url, savedItem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(deleteDto)))
                .andExpect(status().isOk())
                .andDo(document("/items-delete",
                        requestFields(
                                fieldWithPath("writer").description("작성자 계정"),
                                fieldWithPath("password").description("작성자 비밀번호")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("ID")
                        )));

        // then

    }

}