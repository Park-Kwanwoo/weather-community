package org.project.weathercommunity.response.member;

import lombok.Builder;
import lombok.Getter;
import org.project.weathercommunity.domain.member.Member;

@Getter
public class MemberMypageResponse {

    private final String email;

    private final String nickname;
    private final String phone;

    public MemberMypageResponse(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.phone = member.getPhone();
    }

    @Builder
    public MemberMypageResponse(String email, String nickname, String phone) {
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
    }
}
