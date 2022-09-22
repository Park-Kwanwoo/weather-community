package org.project.weathercommunity.response.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginResponse {

    private final Long id;
    private final String email;

    private final String token;


    @Builder
    public MemberLoginResponse(Long id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }
}
