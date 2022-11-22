package org.project.weathercommunity.request.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class MemberLogin {

    @NotBlank(message = "아이디를 입력해주세요")
    private String email;

    private String password;

    private String auth;

    @Builder
    public MemberLogin(String email, String password, String auth) {
        this.email = email;
        this.password = password;
        this.auth = auth;
    }
}
