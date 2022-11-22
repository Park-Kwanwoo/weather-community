package org.project.weathercommunity.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.weathercommunity.domain.base.BaseTimeEntity;
import org.project.weathercommunity.domain.comment.Comment;
import org.project.weathercommunity.domain.post.Post;
import org.project.weathercommunity.domain.token.Token;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String auth;

    @OneToMany(orphanRemoval = true, mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(orphanRemoval = true, mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(orphanRemoval = true, mappedBy = "member")
    private Token token;

    @Builder
    public Member(String email, String password, String nickname, String auth) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = Role.ROLE_USER;
        this.auth = auth;
    }

    public MemberEditor.MemberEditorBuilder toEditor() {
        return MemberEditor.builder()
                .nickname(nickname);
    }

    public void edit(MemberEditor memberEditor) {
        this.nickname = memberEditor.getNickname();
    }

    public PasswordEditor.PasswordEditorBuilder toPasswordEditor() {
        return PasswordEditor.builder()
                .password(password);
    }
    public void passwordEdit(PasswordEditor passwordEditor) {
        this.password = passwordEditor.getPassword();
    }
}
