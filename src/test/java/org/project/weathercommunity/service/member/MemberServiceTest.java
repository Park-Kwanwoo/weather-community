package org.project.weathercommunity.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.Role;
import org.project.weathercommunity.exception.MemberNotFound;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        memberService.join(memberCreate);

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
        Member member = Member.builder()
                .email("test@case.com")
                .name("테스터")
                .phone("010-1234-5678")
                .password("tester")
                .build();

        memberRepository.save(member);

        MemberEdit memberEdit = MemberEdit.builder()
                .phone("010-9876-5432")
                .name("테스터훈")
                .password("qwer1234")
                .build();

        // when
        memberService.edit(member.getId(), memberEdit);

        // then
        Member changedMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new RuntimeException("멤버가 존재하지 않습니다. id=" + member.getId()));

        assertEquals("010-9876-5432", changedMember.getPhone());
        assertEquals("qwer1234", changedMember.getPassword());
        assertEquals("테스터훈", changedMember.getName());
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void 회원_삭제_테스트() {

        // given
        Member member = Member.builder()
                .email("test@case.com")
                .name("테스터")
                .phone("010-1234-5678")
                .password("tester")
                .build();

        memberRepository.save(member);

        // when
        memberService.delete(member.getId());

        // then
        assertThrows(MemberNotFound.class, () -> {
           memberService.delete(member.getId());
        });
    }
}