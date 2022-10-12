package org.project.weathercommunity.response.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.project.weathercommunity.domain.comment.Comment;

@Getter
@Setter
public class CommentResponse {

    private final Long id;
    private final String content;
    private final String createTime;
    private final Long postId;
    private final String nickname;
    private final Long memberId;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createTime = comment.getCreatedDate().substring(5, 16);
        this.postId = comment.getPost().getId();
        this.nickname = comment.getMember().getNickname();
        this.memberId = comment.getMember().getId();
    }

    @Builder
    public CommentResponse(Long id, String content, String createTime, Long postId, String nickname, Long memberId) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.postId = postId;
        this.nickname = nickname;
        this.memberId = memberId;
    }
}
