package org.project.weathercommunity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.weathercommunity.domain.Post;
import org.project.weathercommunity.exception.PostNotFound;
import org.project.weathercommunity.repository.PostRepository;
import org.project.weathercommunity.request.PostCreate;
import org.project.weathercommunity.request.PostEdit;
import org.project.weathercommunity.request.PostSearch;
import org.project.weathercommunity.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        PostResponse post = postService.get(request.getId());

        // then
        assertNotNull(post);
        Post response = postRepository.findAll().get(0);
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 1개 조회 실패")
    void 글_단건_조회_실패_케이스() {

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });
    }


    @Test
    @DisplayName("post 페이징 1페이지 조회")

    void POST_페이징_목록_조회() {

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

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("제목30", posts.get(0).getTitle());
        assertEquals("내용26", posts.get(4).getContent());
    }

    @Test
    @DisplayName("글 제목 수정")
    void 글_제목_수정() {

        // given
        Post post = Post.builder()
                .title("수정전")
                .content("내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("수정후")
                .content("내용")
                .build();

        // when
        postService.edit(post.getId(), postEdit);


        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("수정후", changedPost.getTitle());
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void 글_내용_수정() {

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용전")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목")
                .content("내용후")
                .build();

        // when
        postService.edit(post.getId(), postEdit);


        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFound::new);

        assertEquals("내용후", changedPost.getContent());

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });

    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void 게시글_삭제() {

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }
}