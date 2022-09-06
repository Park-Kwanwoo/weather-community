package org.project.weathercommunity.response.member;

import lombok.Builder;
import lombok.Getter;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.Role;

@Getter
public class MemberResponse {

    private final String email;
    private final Role role;

    public MemberResponse(Member member) {
        this.email = member.getEmail();
        this.role = member.getRole();
    }

    @Builder
    public MemberResponse(String email, Role role) {
        this.email = email;
        this.role = role;
    }
}
