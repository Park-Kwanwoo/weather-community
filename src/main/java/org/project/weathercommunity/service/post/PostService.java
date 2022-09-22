package org.project.weathercommunity.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.post.Post;
import org.project.weathercommunity.domain.post.PostEditor;
import org.project.weathercommunity.exception.MemberNotFoundException;
import org.project.weathercommunity.exception.PostNotFoundException;
import org.project.weathercommunity.repository.post.PostRepository;
import org.project.weathercommunity.request.post.PostCreate;
import org.project.weathercommunity.request.post.PostEdit;
import org.project.weathercommunity.request.post.PostSearch;
import org.project.weathercommunity.response.post.PostResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate, Authentication authentication) {

        Member member = (Member) authentication.getPrincipal();

        if (member == null) {
            throw new MemberNotFoundException();
        } else {
            // postCreate -> Entity
            Post post = Post.builder()
                    .title(postCreate.getTitle())
                    .content(postCreate.getContent())
                    .member(member)
                    .build();
            postRepository.save(post);
        }

    }

    public PostResponse get(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .memberId(post.getMember().getId())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public void edit(Long id, PostEdit postedit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder
                .title(postedit.getTitle())
                .content(postedit.getContent())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        postRepository.delete(post);
    }
}