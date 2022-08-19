package org.project.weathercommunity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.weathercommunity.domain.Post;
import org.project.weathercommunity.repository.PostRepository;
import org.project.weathercommunity.request.PostCreate;
import org.project.weathercommunity.request.PostEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("PostController 테스트")
    void test1() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청 시 title 값은 필수")
    void test2() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title(null)
                .content("내용")
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void DB_SAVE_TEST_WHEN_REQUEST_POST() throws Exception {

        // given
        PostCreate request = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목", post.getTitle());
        assertEquals("내용", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회 Controller 테스트")
    void 글_단건_조회() throws Exception {

        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();

        postRepository.save(post);

        // expected (when과 then이 섞인 느낌)
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("123456789012345"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 목록 조회 Controller 테스트")
    void 글_목록_조회() throws Exception {

        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("제목" + i)
                            .content("내용" + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);
        // expected (when과 then이 섞인 느낌)
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("제목30"))
                .andExpect(jsonPath("$[0].content").value("내용30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정 Controller 테스트")
    void 글_제목_수정() throws Exception {

        // given
        Post post = Post.builder()
                .title("제목전")
                .content("내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목후")
                .content("내용")
                .build();

        // expected (when과 then이 섞인 느낌)
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제 Controller 테스트")
    void 게시글_삭제() throws Exception {

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void 존재하지_않는_게시글_조회() throws Exception {

        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void 존재하지_않는_게시글_수정() throws Exception {

        // given
        PostEdit postEdit = PostEdit.builder()
                .title("타이틀")
                .content("내용")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성 시 제목에 '바보'는 포함될 수 없다.")
    void 계시글_값_오류_테스트() throws Exception {

        // given
        PostCreate request = PostCreate.builder()
                .title("바보짱")
                .content("내용")
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}