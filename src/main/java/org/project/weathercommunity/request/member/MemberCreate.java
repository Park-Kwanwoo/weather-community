package org.project.weathercommunity.request.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.project.weathercommunity.domain.member.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberCreate {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "제대로된 이메일 형식으로 입력해주세요.", regexp = "^[\\w\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
             message = "숫자, 문자, 특수문자 포함 8~15자리 이내로 입력해주세요.")
    private String password;
    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(message = "닉네임은 최대 8자리 한글, 숫자, 영어만 가능합니다.", regexp = "^[a-zA-Z/\\d가-힣]{1,8}+$")
    private String nickname;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^(?:(010-\\d{4})|(01[1|6-9]-\\d{3,4}))-(\\d{4})$", message = "전화번호 형식에 맞게 입력해주세요.")
    private String phone;

    private Role role;

    @Builder
    public MemberCreate(String email, String password, String nickname, String phone) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.role = Role.ROLE_USER;
    }
}
