package com.example.market.controller;

import com.example.market.domain.entity.Comment;
import com.example.market.domain.entity.Item;
import com.example.market.dto.comment.request.CommentCreateRequestDto;
import com.example.market.dto.comment.request.CommentDeleteRequestDto;
import com.example.market.dto.comment.request.CommentReplyRequestDto;
import com.example.market.dto.comment.request.CommentUpdateRequestDto;
import com.example.market.repository.CommentRepository;
import com.example.market.repository.ItemRepository;
import com.example.market.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class CommentControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

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

    @DisplayName("댓글 생성 API 테스트")
    @Test
    void createComment() throws Exception {
        // given
        Long itemId = createItem();

        CommentCreateRequestDto createDto = CommentCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .content("내용")
                .build();

        String url = "http://localhost:8080/items/{itemId}/comments";

        // when
        mvc.perform(post(url, itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(document("/comments",
                        requestFields(
                                fieldWithPath("writer").description("작성자"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("content").description("내용")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("아이템 ID")
                        )
                ));

        // then

    }

    @DisplayName("댓글 수정 API 테스트")
    @Test
    void updateComment() throws Exception {
        // given
        Long itemId = createItem();
        Long commentId = createComment(itemId);

        CommentUpdateRequestDto updateDto = CommentUpdateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .content("수정내용")
                .build();

        String url = "http://localhost:8080/items/{itemId}/comments/{commentId}";

        // when
        mvc.perform(put(url, itemId, commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(document("/comments-update",
                        requestFields(
                                fieldWithPath("writer").description("댓글 작성자"),
                                fieldWithPath("password").description("댓글 작성자 비밀번호"),
                                fieldWithPath("content").description("수정 내용")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("아이템 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        )));

        // then

    }

    @DisplayName("댓글 삭제 API 테스트")
    @Test
    void deleteComment() throws Exception {
        // given
        Long itemId = createItem();
        Long commentId = createComment(itemId);

        CommentDeleteRequestDto deleteDto = CommentDeleteRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .build();

        String url = "http://localhost:8080/items/{itemId}/comments/{commentId}";

        // when
        mvc.perform(delete(url, itemId, commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(deleteDto)))
                .andExpect(status().isOk())
                .andDo(document("/comments-delete",
                        requestFields(
                                fieldWithPath("writer").description("작성자"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("아이템 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        )));

        // then

    }

    @DisplayName("댓글 답글(reply) 수정 API")
    @Test
    void updateCommentReply() throws Exception {
        // given
        Long itemId = createItem();
        Long commentId = createComment(itemId);

        CommentReplyRequestDto replyDto = CommentReplyRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .reply("답글내용")
                .build();

        String url = "http://localhost:8080/items/{itemId}/comments/{commentId}/reply";

        // when
        mvc.perform(put(url, itemId, commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(replyDto)))
                .andExpect(status().isOk())
                .andDo(document("/comments-reply",
                        requestFields(
                                fieldWithPath("writer").description("아이템 글 작성자"),
                                fieldWithPath("password").description("아이템 글 비밀번호"),
                                fieldWithPath("reply").description("답글내용")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("아이템 글 번호"),
                                parameterWithName("commentId").description("댓글 번호")
                        )));

        // then

    }

    private Long createComment(Long itemId) {
        CommentCreateRequestDto dto = CommentCreateRequestDto.builder()
                .writer("작성자")
                .password("비밀번호")
                .content("내용")
                .build();

        Comment comment = commentRepository.save(dto.toEntity(itemId));

        return comment.getId();
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
}