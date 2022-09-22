package org.project.weathercommunity.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.weathercommunity.domain.base.BaseTimeEntity;
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

    private String name;

    private String phone;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(orphanRemoval = true, mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToOne(orphanRemoval = true, mappedBy = "member")
    private Token token;

    @Builder
    public Member(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = Role.ROLE_USER;
    }

    public MemberEditor.MemberEditorBuilder toEditor() {
        return MemberEditor.builder()
                .name(name)
                .password(password)
                .phone(phone);
    }

    public void edit(MemberEditor memberEditor) {
        this.name = memberEditor.getName();
        this.phone = memberEditor.getPhone();
        this.password = memberEditor.getPassword();
    }

}
