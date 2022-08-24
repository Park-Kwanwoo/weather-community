package org.project.weathercommunity.service.member;

import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.Role;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public void signUp(MemberCreate memberCreate) {

        Member member = Member.builder()
                .email(memberCreate.getEmail())
                .name(memberCreate.getName())
                .password(memberCreate.getPassword())
                .phone(memberCreate.getPhone())
                .build();

        memberRepository.save(member);

    }
}
