package org.project.weathercommunity.service.comment;

import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.domain.comment.Comment;
import org.project.weathercommunity.domain.comment.CommentEditor;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.post.Post;
import org.project.weathercommunity.exception.MemberNotFoundException;
import org.project.weathercommunity.exception.PostNotFoundException;
import org.project.weathercommunity.repository.comment.CommentRepository;
import org.project.weathercommunity.repository.post.PostRepository;
import org.project.weathercommunity.request.comment.CommentCreate;
import org.project.weathercommunity.request.comment.CommentEdit;
import org.project.weathercommunity.response.comment.CommentResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void write(CommentCreate commentCreate, Member member) {

        Post post = postRepository.findById(commentCreate.getPostId())
                .orElseThrow(PostNotFoundException::new);

        if (member == null) {
            throw new MemberNotFoundException();
        } else {
            // commentCreate -> Entity
            Comment comment = Comment.builder()
                    .content(commentCreate.getContent())
                    .post(post)
                    .member(member)
                    .build();

            commentRepository.save(comment);
        }

    }

    @Transactional
    public List<CommentResponse> getList(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return commentRepository.getList(post).stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    public void edit(Long id, CommentEdit commentEdit) {



        Comment comment = commentRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        CommentEditor.CommentEditorBuilder commentEditorBuilder = comment.toEditor();

        CommentEditor commentEditor = commentEditorBuilder
                .content(commentEdit.getContent())
                .build();

        comment.edit(commentEditor);
    }

    public void delete(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        commentRepository.delete(comment);
    }
}
