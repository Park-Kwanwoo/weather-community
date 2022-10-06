package org.project.weathercommunity.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.config.security.token.JwtTokenProvider;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.MemberEditor;
import org.project.weathercommunity.domain.member.PasswordEditor;
import org.project.weathercommunity.exception.MemberNotFoundException;
import org.project.weathercommunity.exception.PasswordNotMatchException;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.project.weathercommunity.request.member.PasswordEdit;
import org.project.weathercommunity.response.member.MemberMypageResponse;
import org.project.weathercommunity.service.token.TokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void join(MemberCreate memberCreate) {

        Member member = Member.builder()
                .email(memberCreate.getEmail())
                .name(memberCreate.getName())
                .password(passwordEncoder.encode(memberCreate.getPassword()))
                .phone(memberCreate.getPhone())
                .build();

        memberRepository.save(member);

    }

    @Transactional
    public void edit(Long id, MemberEdit memberEdit) {

        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        MemberEditor.MemberEditorBuilder editorBuilder = member.toEditor();

        MemberEditor memberEditor = editorBuilder
                .name(memberEdit.getName())
                .phone(memberEdit.getPhone())
                .build();


        member.edit(memberEditor);
    }

    public void delete(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        memberRepository.delete(member);
    }

    public MemberMypageResponse get(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        return MemberMypageResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .phone(member.getPhone())
                .build();

    }

    public boolean duplicateCheck(String email) {
        return memberRepository.existsByEmail(email);
    }

    public void logout(HttpServletRequest request) {

        String accessToken = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        Member member = (Member) authentication.getPrincipal();
        tokenService.deleteToken(member);
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void passwordEdit(Long id, PasswordEdit passwordEdit) {

        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        if (passwordEncoder.matches(passwordEdit.getOldPassword(), member.getPassword())) {
            PasswordEditor.PasswordEditorBuilder editorBuilder = member.toPasswordEditor();

            PasswordEditor passwordEditor = editorBuilder
                    .password(passwordEncoder.encode(passwordEdit.getNewPassword()))
                    .build();

            member.passwordEdit(passwordEditor);

        } else {
            throw new PasswordNotMatchException("password", "비밀번호가 일치하지 않습니다.");
        }
    }
}
