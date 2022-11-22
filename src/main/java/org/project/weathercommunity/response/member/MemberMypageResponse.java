package org.project.weathercommunity.response.member;

import lombok.Builder;
import lombok.Getter;
import org.project.weathercommunity.domain.member.Member;

@Getter
public class MemberMypageResponse {

    private final String email;

    private final String nickname;

    public MemberMypageResponse(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }

    @Builder
    public MemberMypageResponse(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
