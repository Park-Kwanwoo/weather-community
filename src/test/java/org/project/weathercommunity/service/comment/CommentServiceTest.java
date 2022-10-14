package org.project.weathercommunity.service.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.weathercommunity.domain.comment.Comment;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.post.Post;
import org.project.weathercommunity.repository.comment.CommentRepository;
import org.project.weathercommunity.repository.post.PostRepository;
import org.project.weathercommunity.request.comment.CommentCreate;
import org.project.weathercommunity.response.comment.CommentResponse;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    PostRepository postRepository;

    @Spy
    @InjectMocks
    CommentService commentService;

    @Mock
    List<CommentResponse> comments;

    CommentCreate commentCreate = mock(CommentCreate.class);
    Comment comment = mock(Comment.class);

    Member member = mock(Member.class);
    Post post = mock(Post.class);

    @Test
    @DisplayName("댓글 작성")
    void COMMENT_WRITE(@Mock Authentication authentication) {

        // given
        willReturn(member).given(authentication).getPrincipal();
        willReturn(comment).given(commentRepository).save(any());

        // when
        commentService.write(commentCreate, authentication);

        // then
        then(commentService).should(times(1)).write(commentCreate, authentication);
        then(postRepository).should(times(1)).getReferenceById(any());

        Comment savedComment = commentRepository.save(comment);

        assertEquals(savedComment.getContent(), commentCreate.getContent());
    }

    @Test
    @DisplayName("게시글 댓글 리스트 조회")
    void COMMENT_GET_LIST_BY_POST_ID() {

        // given

        willReturn(Optional.of(post)).given(postRepository).findById(any());
        willReturn(comments).given(commentRepository).getList(post);


        // when
        commentService.getList(post.getId());

        // then
        then(commentRepository).should(atLeastOnce()).getList(post);

    }

    @Test
    @DisplayName("댓글 삭제")
    void DELETE_COMMENT() {

        // given
        willReturn(Optional.of(comment)).given(commentRepository).findById(anyLong());

        // when
        commentService.delete(anyLong());

        // then
        then(commentRepository).should(atLeastOnce()).delete(any(Comment.class));
    }

}
