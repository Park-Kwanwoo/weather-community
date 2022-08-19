package org.project.weathercommunity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.weathercommunity.domain.Post;
import org.project.weathercommunity.repository.PostRepository;
import org.project.weathercommunity.request.PostCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.weathercommunity.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

// API 문서 생성

// GET /posts/{postId} -> 단건 조회
// POST /posts -> 게시글 등록

// 클라이언트 입장 어떤 API 있는지 모름

// Spring RestDocs
// Test 케이스 실행 -> 문서를 생성

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("글 단건 조회 테스트")
    void 글_단건_조회_Doc() throws Exception {

        // given
        Post request = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(request);

        // expected
        this.mockMvc.perform(get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry", pathParameters(
                        parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )

                ));
    }

    @Test
    @DisplayName("글 등록")
    void 글_등록_Doc() throws Exception {

        // given
        PostCreate request = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        this.mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("제목")
                                        .attributes(key("constraint").value("예제")),
                                fieldWithPath("content").description("내용").optional()
                        )
                ));
    }
}
