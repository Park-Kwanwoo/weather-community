package org.project.weathercommunity.request.token;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.project.weathercommunity.domain.member.Member;

@Getter
@Setter
public class TokenRequest {

    private final String accessToken;
    private final String refreshToken;

    @Builder
    public TokenRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
