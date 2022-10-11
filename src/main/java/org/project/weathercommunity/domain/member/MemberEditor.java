package org.project.weathercommunity.domain.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberEditor {

    private final String nickname;
    private final String phone;

    @Builder
    public MemberEditor(String nickname, String phone) {
        this.nickname = nickname;
        this.phone = phone;
    }
}
