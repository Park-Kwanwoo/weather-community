package org.project.weathercommunity.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.project.weathercommunity.service.member.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/join")
    public void join(@RequestBody @Valid MemberCreate memberCreate) {
        memberService.join(memberCreate);
    }

    @PatchMapping("/members/{memberId}")
    public void edit(@PathVariable("memberId") Long id, @RequestBody @Valid MemberEdit memberEdit) {
        memberService.edit(id, memberEdit);
    }

    @DeleteMapping("/members/{memberId}")
    public void delete(@PathVariable("memberId") Long id) {
        memberService.delete(id);
    }
}
