package org.project.weathercommunity.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.weathercommunity.domain.Post;
import org.project.weathercommunity.repository.PostRepository;
import org.project.weathercommunity.request.PostCreate;
import org.project.weathercommunity.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void 글_작성() {

        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목", post.getTitle());
        assertEquals("내용", post.getContent());
    }

    @Test
    @DisplayName("post 단건 조회")
    void POST_단건_조회() {

        // given
        Post request = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(request);

        // 클라이언트 요구사항
            // json 응답에서 title값 길이를 최대 10글자로 제한.

        // when
        PostResponse post = postService.get(1L);

        // then
        assertNotNull(post);
        Post response = postRepository.findAll().get(0);
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }
}