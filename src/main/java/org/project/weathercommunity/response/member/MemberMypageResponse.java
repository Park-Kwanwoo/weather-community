package org.project.weathercommunity.response.member;

import lombok.Builder;
import lombok.Getter;
import org.project.weathercommunity.domain.member.Member;

@Getter
public class MemberMypageResponse {

    private final Long id;
    private final String email;

    private final String password;
    private final String name;
    private final String phone;

    public MemberMypageResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.name = member.getName();
        this.phone = member.getPhone();
    }

    @Builder
    public MemberMypageResponse(Long id, String email, String password, String name, String phone) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }
}
