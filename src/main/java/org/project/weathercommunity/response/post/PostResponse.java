package org.project.weathercommunity.response.post;

import lombok.Builder;
import lombok.Getter;
import org.project.weathercommunity.domain.post.Post;

/**
 * 서비스 정책에 맞는 응답 클래스
 */
@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Long memberId;

    // 생성자 오버로딩
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.memberId = post.getMember().getId();
    }
    @Builder
    public PostResponse(Long id, String title, String content, Long memberId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
}
