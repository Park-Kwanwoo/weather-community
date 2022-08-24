package org.project.weathercommunity.request.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.project.weathercommunity.domain.member.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberCreate {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "제대로된 이메일 형식으로 입력해주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;

    private Role role;

    @Builder
    public MemberCreate(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = Role.ROLE_USER;
    }
}
