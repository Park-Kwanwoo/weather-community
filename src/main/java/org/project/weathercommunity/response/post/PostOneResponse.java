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
    private final String memberEmail;

    public PostOneResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdTime = post.getCreatedDate().substring(5, 16);
        this.memberEmail = post.getMember().getEmail();
    }

    @Builder
    public PostOneResponse(Long id, String title, String content, String createdTime, String memberEmail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.memberEmail = memberEmail;
    }
}
