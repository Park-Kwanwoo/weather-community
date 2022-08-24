package org.project.weathercommunity.domain.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberEditor {

    private final String name;
    private final String phone;
    private final String password;

    @Builder
    public MemberEditor(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
    }
}
