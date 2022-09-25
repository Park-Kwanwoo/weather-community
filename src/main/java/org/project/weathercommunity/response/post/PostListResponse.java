package org.project.weathercommunity.response.post;

import lombok.Builder;
import lombok.Getter;
import org.project.weathercommunity.domain.post.Post;

/**
 * 서비스 정책에 맞는 응답 클래스
 */
@Getter
public class PostListResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String createdTime;
    private final String memberName;

    // 생성자 오버로딩
    public PostListResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdTime = post.getCreatedDate().substring(5, 16);
        this.memberName = post.getMember().getName();
    }
    @Builder
    public PostListResponse(Long id, String title, String content, String createdTime, String memberName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.memberName = memberName;
    }
}
