package org.project.weathercommunity.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.weathercommunity.domain.base.BaseTimeEntity;
import org.project.weathercommunity.domain.member.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Lob
    private String content;

    @ManyToOne(targetEntity = Member.class, fetch = EAGER)
    private Member member;

    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
