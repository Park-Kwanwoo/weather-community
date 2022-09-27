package org.project.weathercommunity.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.exception.MemberDuplicateException;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.project.weathercommunity.response.member.MemberMypageResponse;
import org.project.weathercommunity.service.member.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/join")
    public void join(@RequestBody @Valid MemberCreate memberCreate) {

        if (!memberService.duplicateCheck(memberCreate.getEmail())) {
            memberService.join(memberCreate);
        } else {
            throw new MemberDuplicateException("email", "중복된 이메일입니다.");
        }

    }
    @GetMapping("/members/{memberId}")
    public MemberMypageResponse get(@PathVariable("memberId") Long id) {
        return memberService.get(id);
    }

    @PatchMapping("/members/{memberId}")
    public void edit(@PathVariable("memberId") Long id, @RequestBody @Valid MemberEdit memberEdit) {
        memberService.edit(id, memberEdit);
    }

    @DeleteMapping("/members/{memberId}")
    public void delete(@PathVariable("memberId") Long id) {
        memberService.delete(id);
    }

    @GetMapping("/members/logout")
    public void logout(HttpServletRequest request) {
        memberService.logout(request);
    }
}
