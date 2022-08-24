package org.project.weathercommunity.service.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.Role;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("회원 가입")
    void 회원_가입() {

        // given
        MemberCreate memberCreate = MemberCreate.builder()
                .email("test@case.com")
                .name("테스터")
                .phone("010-1234-5678")
                .password("tester")
                .build();

        // when
        memberService.signUp(memberCreate);

        // then
        assertEquals(1L, memberRepository.count());
        Member member = memberRepository.findAll().get(0);
        assertEquals("test@case.com", member.getEmail());
        assertEquals("tester", member.getPassword());
        assertEquals("테스터", member.getName());
        assertEquals("010-1234-5678", member.getPhone());
        assertEquals(Role.ROLE_USER, member.getRole());
    }


    @Test
    @DisplayName("회원 정보 수정")
    void 회원_정보_수정() {

        // given


        // when


        // then
    }
}