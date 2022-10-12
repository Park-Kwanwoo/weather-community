package org.project.weathercommunity.service.member;

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
import org.project.weathercommunity.response.member.MemberMypageResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Spy
    @InjectMocks
    MemberService memberService;

    @Spy
    BCryptPasswordEncoder passwordEncoder;


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
    void MEMBER_JOIN() {

        // given
        MemberCreate memberCreate = memberCreateRequest();
        Member member = createMemberEntity(memberCreate);


        // Member 객체 어떠한 것을 저장하더라도 개발자가 정해놓은 member를 반환
        willReturn(member).given(memberRepository).save(any(Member.class));

        // when
        memberService.join(memberCreate);
        Member savedMember = memberRepository.save(member);

        // then
        then(memberService).should().join(argThat(memberRequest -> memberRequest.getEmail().equals("test@case.com")));
        then(memberService).should().join(argThat(memberRequest -> memberRequest.getEmail().equals("test@case.com")));
        then(memberService).should().join(argThat(memberRequest -> memberRequest.getNickname().equals("아이스아메리카노")));
        then(memberService).should().join(argThat(memberRequest -> memberRequest.getPassword().equals("qwer123$")));
        then(memberService).should().join(argThat(memberRequest -> memberRequest.getPhone().equals("010-1234-1234")));

        then(memberService).should(times(1)).join(memberCreate);


        assertEquals(memberCreate.getEmail(), savedMember.getEmail());
        assertEquals(memberCreate.getNickname(), savedMember.getNickname());
        assertEquals(memberCreate.getPhone(), savedMember.getPhone());
        assertTrue(passwordEncoder.matches(memberCreate.getPassword(), savedMember.getPassword()));

    }

    @Test
    @DisplayName("회원 비밀번호 수정")
    void MEMBER_PASSWORD_EDIT() {

        // given
        MemberCreate memberCreate = memberCreateRequest();
        Member member = createMemberEntity(memberCreate);


        PasswordEdit passwordEdit = PasswordEdit.builder()
                .oldPassword("qwer123$")
                .newPassword("tester123$")
                .build();

        willReturn(Optional.of(member)).given(memberRepository).findById(any());

        // when
        memberService.passwordEdit(member.getId(), passwordEdit);

        // then
        assertTrue(passwordEncoder.matches(passwordEdit.getNewPassword(), member.getPassword()));
    }

    @Test
    @DisplayName("회원 정보 수정")
    void MEMBER_EDIT() {

        // given
        MemberCreate memberCreate = memberCreateRequest();
        Member member = createMemberEntity(memberCreate);

        MemberEdit memberEdit = MemberEdit.builder()
                .nickname("강낭콩")
                .build();

        // when
        willReturn(Optional.of(member)).given(memberRepository).findById(any());

        memberService.edit(member.getId(), memberEdit);

        // then
        Member changedMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);


        assertEquals("강낭콩", changedMember.getNickname());
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void MEMBER_DELETE() {

        // given
        MemberCreate memberCreate = memberCreateRequest();
        Member member = createMemberEntity(memberCreate);

        willReturn(Optional.of(member)).given(memberRepository).findById(any());

        // when
        memberService.delete(member.getId());

        // then
        then(memberRepository).should(times(1)).findById(any());
        then(memberRepository).should(times(1)).delete(member);

    }

    @Test
    @DisplayName("회원 개인 정보")
    void MEMBER_PERSONAL_INFO() {

        // given
        MemberCreate memberCreate = memberCreateRequest();
        Member member = createMemberEntity(memberCreate);


        willReturn(Optional.of(member)).given(memberRepository).findById(any());

        // when
        MemberMypageResponse memberResponse = memberService.get(member.getId());

        // then
        then(memberRepository).should(times(1)).findById(any());

        assertEquals(member.getEmail(), memberResponse.getEmail());
        assertEquals(member.getNickname(), memberResponse.getNickname());
        assertEquals(member.getPhone(), memberResponse.getPhone());

    }
}