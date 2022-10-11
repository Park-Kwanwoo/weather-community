package org.project.weathercommunity.request.member;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEdit {

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(message = "닉네임은 최대 8자리 한글, 숫자, 영어만 가능합니다.", regexp = "^[a-zA-Z/\\d가-힣]{1,8}+$")
    private String nickname;

    @Builder
    public MemberEdit(String nickname) {
        this.nickname = nickname;
    }
}
