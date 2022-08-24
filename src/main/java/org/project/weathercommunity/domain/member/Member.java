package org.project.weathercommunity.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.weathercommunity.domain.base.BaseTimeEntity;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String email;

    private String password;

    private String name;

    private String phone;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = Role.ROLE_USER;
    }
}
