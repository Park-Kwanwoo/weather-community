package org.project.weathercommunity.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.exception.MemberDuplicateException;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.project.weathercommunity.response.member.MemberMypageResponse;
import org.project.weathercommunity.service.member.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/members/{memberEmail}")
    public MemberMypageResponse get(@PathVariable("memberEmail") String email) {
        return memberService.get(email);
    }

    @PatchMapping("/members/{memberEmail}")
    public void edit(@PathVariable("memberEmail") String email, @RequestBody @Valid MemberEdit memberEdit) {
        memberService.edit(email, memberEdit);
    }

    @DeleteMapping("/members/{memberEmail}")
    public void delete(@PathVariable("memberEmail") String email) {
        memberService.delete(email);
    }

}
