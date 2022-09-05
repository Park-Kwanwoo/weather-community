package org.project.weathercommunity.response.member;

import lombok.Builder;
import lombok.Getter;
import org.project.weathercommunity.domain.member.Member;

@Getter
public class MemberResponse {

    private final String email;
    private final String password;
    private final String name;
    private final String phone;

    public MemberResponse(Member member) {
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.name = member.getName();
        this.phone = member.getPhone();
    }

    @Builder
    public MemberResponse(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }
}
