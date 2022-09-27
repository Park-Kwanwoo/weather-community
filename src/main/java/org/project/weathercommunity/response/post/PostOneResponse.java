package org.project.weathercommunity.response.post;

import lombok.Builder;
import lombok.Getter;
import org.project.weathercommunity.domain.post.Post;

@Getter
public class PostOneResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String createdTime;
    private final Long memberId;

    public PostOneResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdTime = post.getCreatedDate().substring(5, 16);
        this.memberId = post.getMember().getId();
    }

    @Builder
    public PostOneResponse(Long id, String title, String content, String createdTime, Long memberId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.memberId = memberId;
    }
}
