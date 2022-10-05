package org.project.weathercommunity.domain.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PasswordEditor {

    private final String password;

    @Builder
    public PasswordEditor(String password) {
        this.password = password;
    }
}
