package org.project.weathercommunity.domain.comment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.weathercommunity.domain.base.BaseTimeEntity;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.post.Post;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    @ManyToOne(targetEntity = Post.class, fetch = LAZY)
    private Post post;

    @ManyToOne(targetEntity = Member.class, fetch = LAZY)
    private Member member;

    @Builder
    public Comment(Long id, String content, Post post, Member member) {
        this.id = id;
        this.content = content;
        this.post = post;
        this.member = member;
    }

    public CommentEditor.CommentEditorBuilder toEditor() {
        return CommentEditor.builder()
                .content(content);
    }

    public void edit(CommentEditor commentEditor) {
        content = commentEditor.getContent();
    }
}
