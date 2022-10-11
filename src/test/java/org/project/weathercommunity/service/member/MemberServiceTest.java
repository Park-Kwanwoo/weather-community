package org.project.weathercommunity.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.exception.MemberNotFoundException;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.project.weathercommunity.request.member.PasswordEdit;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Spy
    @InjectMocks
    MemberService memberService;

    @Mock
    PasswordEncoder passwordEncoder;


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    private Member createMemberEntity(MemberCreate memberCreate) {
        return Member.builder()
                .email(memberCreate.getEmail())
                .nickname(memberCreate.getNickname())
                .password(passwordEncoder.encode(memberCreate.getPassword()))
                .phone(memberCreate.getPhone())
                .build();
    }

    private MemberCreate memberCreateRequest() {

        return MemberCreate.builder()
                .email("test@case.com")
                .nickname("아이스아메리카노")
                .password("qwer123$")
                .phone("010-1234-1234")
                .build();
    }

    @Test
    @DisplayName("회원 가입")
    void Member_Join() {

        // given
        MemberCreate memberCreate = memberCreateRequest();
        Member member = createMemberEntity(memberCreate);

        Long fakeMemberId = 1L;
        ReflectionTestUtils.setField(member, "id", fakeMemberId);

        given(memberRepository.save(any()))
                .willReturn(member);

        // when
        memberService.join(memberCreate);

        // then
        // verify: Method 실행 확인
        verify(memberService, times(1)).join(argThat(memberRequest -> memberRequest.getEmail().equals("test@case.com")));
        verify(memberService, times(1)).join(argThat(memberRequest -> memberRequest.getEmail().equals("test@case.com")));
        verify(memberService, times(1)).join(argThat(memberRequest -> memberRequest.getNickname().equals("아이스아메리카노")));
        verify(memberService, times(1)).join(argThat(memberRequest -> memberRequest.getPassword().equals("qwer123$")));
        verify(memberService, times(1)).join(argThat(memberRequest -> memberRequest.getPhone().equals("010-1234-1234")));

    }


    @Test
    @DisplayName("회원 비밀번호 수정")
    void 회원_비밀번호_수정() {

        // given
        Member member = Member.builder()
                .email("test@case.com")
                .nickname("테스터")
                .phone("010-1234-5678")
                .password(passwordEncoder.encode("tester123$"))
                .build();

        memberRepository.save(member);

        PasswordEdit passwordEdit = PasswordEdit.builder()
                .oldPassword("tester123$")
                .newPassword("qwer123$")
                .build();

        // when
        memberService.passwordEdit(member.getId(), passwordEdit);

        // then
        Member changedMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);

        assertTrue(passwordEncoder.matches("qwer123$", changedMember.getPassword()));
    }


    @Test
    @DisplayName("회원 정보 수정")
    void 회원_정보_수정() {

        // given
        Member member = Member.builder()
                .nickname("테스터")
                .email("test@email.com")
                .password(passwordEncoder.encode("tester123$"))
                .phone("010-1111-1111")
                .build();

        memberRepository.save(member);

        // when
        MemberEdit memberEdit = MemberEdit.builder()
                .nickname("강낭콩")
                .build();

        memberService.edit(member.getId(), memberEdit);

        // then
        Member changedMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);


        assertAll(
                () -> assertEquals("강낭콩", changedMember.getNickname()),
                () -> assertEquals("010-2222-2222", changedMember.getPhone())
        );
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void 회원_삭제_테스트() {

        // given
        Member member = Member.builder()
                .email("test@case.com")
                .nickname("테스터")
                .phone("010-1234-5678")
                .password("tester")
                .build();

        memberRepository.save(member);

        // when
        memberService.delete(member.getId());

        // then
        assertThrows(MemberNotFoundException.class, () -> {
           memberService.delete(member.getId());
        });
    }
}