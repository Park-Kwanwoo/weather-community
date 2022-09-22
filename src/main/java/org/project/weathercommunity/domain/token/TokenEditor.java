package org.project.weathercommunity.domain.token;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenEditor {

    private final String accessToken;
    private final String refreshToken;

    @Builder
    public TokenEditor(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
