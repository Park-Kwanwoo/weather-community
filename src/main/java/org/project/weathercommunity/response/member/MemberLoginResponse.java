package org.project.weathercommunity.response.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginResponse {

    private final Long id;

    private final String token;


    @Builder
    public MemberLoginResponse(Long id, String token) {
        this.id = id;
        this.token = token;
    }
}
