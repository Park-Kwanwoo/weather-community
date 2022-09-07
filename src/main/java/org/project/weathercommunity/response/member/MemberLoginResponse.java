package org.project.weathercommunity.response.member;

import lombok.Builder;
import lombok.Getter;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.Role;

@Getter
public class MemberLoginResponse {

    private final Long id;
    private final String email;
    private final Role role;


    public MemberLoginResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.role = member.getRole();
    }

    @Builder
    public MemberLoginResponse(Long id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
