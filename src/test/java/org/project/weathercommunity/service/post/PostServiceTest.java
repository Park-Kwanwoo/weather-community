package org.project.weathercommunity.service.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.post.Post;
import org.project.weathercommunity.exception.PostNotFoundException;
import org.project.weathercommunity.repository.post.PostRepository;
import org.project.weathercommunity.request.post.PostCreate;
import org.project.weathercommunity.request.post.PostEdit;
import org.project.weathercommunity.request.post.PostSearch;
import org.project.weathercommunity.response.post.PostListResponse;
import org.project.weathercommunity.response.post.PostOneResponse;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Spy
    @InjectMocks
    private PostService postService;

    @Mock
    Authentication authentication;

    Member member = Member.builder()
            .email("test@case.com")
            .nickname("아무개")
            .build();

    private PostCreate postCreate() {
        return PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();
    }

    private Post toPostEntity(PostCreate postCreate) {
        return Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .member(member)
                .build();
    }

    @Test
    @DisplayName("글 작성")
    void POST_WRITE() {

        // given
        PostCreate postCreate = postCreate();
        Post post = toPostEntity(postCreate);

        willReturn(post).given(postRepository).save(any());

        // when
        postService.write(postCreate, member);

        // then
        assertEquals(postRepository.save(post).getContent(), postCreate.getContent());
        assertEquals(postRepository.save(post).getTitle(), postCreate.getTitle());
    }

    @Test
    @DisplayName("POST 단건 조회")
    void POST_GET_ONE() {

        // given
        PostCreate postCreate = postCreate();
        Post post = toPostEntity(postCreate);

        willReturn(Optional.of(post)).given(postRepository).findById(any());

        // when
        PostOneResponse postResponse = postService.get(any());

        // then
        assertEquals(postResponse.getContent(), postCreate.getContent());
        assertEquals(postResponse.getTitle(), postCreate.getTitle());
    }

    @Test
    @DisplayName("글 단건 조회 실패")
    void FAILED_CASE_WHEN_GET_SINGLE_POST() {

        // given
        willThrow(new PostNotFoundException()).given(postRepository).findById(any());

        // expected
        assertThrows(PostNotFoundException.class, () -> postService.get(any()));

    }


    @Test
    @DisplayName("post 페이징 1페이지 조회")
    void POST_GET_PAGING_LIST() {

        // given
        List<PostListResponse> responsePost = IntStream.range(1, 11)
                .mapToObj(i -> {
                    return PostListResponse.builder()
                            .title("제목" + (11 - i))
                            .content("내용" + (11 - i))
                            .build();
                })
                .collect(Collectors.toList());

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        willReturn(responsePost).given(postService).getList(postSearch);

        // when
        List<PostListResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("제목10", posts.get(0).getTitle());
        assertEquals("내용6", posts.get(4).getContent());
    }

    @Test
    @DisplayName("글 수정")
    void POST_EDIT() {

        // given
        PostCreate postCreate = postCreate();
        Post post = toPostEntity(postCreate);

        willReturn(Optional.of(post)).given(postRepository).findById(any());

        PostEdit postEdit = PostEdit.builder()
                .title("변경 제목")
                .content("변경 내용")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        assertEquals("변경 제목", post.getTitle());
        assertEquals("변경 내용", post.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void POST_DELETE() {

        // given
        willDoNothing().given(postService).delete(any());

        // when
        postService.delete(1L);

        // then
        then(postService).should(times(1)).delete(any());
    }
}