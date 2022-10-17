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
import org.project.weathercommunity.response.post.PostListResponse;
import org.project.weathercommunity.response.post.PostOneResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate, Member member) {

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

    @Transactional
    public PostOneResponse get(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        return PostOneResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdTime(post.getCreatedDate())
                .memberId(post.getMember().getId())
                .build();
    }

    @Transactional
    public List<PostListResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostListResponse::new)
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

    public long totalPage() {
        return postRepository.count();
    }
}