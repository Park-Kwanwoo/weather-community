package org.project.weathercommunity.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.config.security.token.JwtTokenProvider;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.MemberEditor;
import org.project.weathercommunity.domain.member.PasswordEditor;
import org.project.weathercommunity.exception.MemberEmailDuplicationException;
import org.project.weathercommunity.exception.MemberNicknameDuplicationException;
import org.project.weathercommunity.exception.MemberNotFoundException;
import org.project.weathercommunity.exception.PasswordNotMatchException;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.project.weathercommunity.request.member.PasswordEdit;
import org.project.weathercommunity.response.member.MemberMypageResponse;
import org.project.weathercommunity.service.token.TokenService;
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
                .nickname(memberCreate.getNickname())
                .password(memberCreate.getPassword() != null ? passwordEncoder.encode(memberCreate.getPassword()) : null)
                .auth(memberCreate.getAuth())
                .build();

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new MemberEmailDuplicationException("email", "?????? ???????????? ??????????????????.");
        }  else {
            memberRepository.save(member);
        }
    }

    @Transactional
    public void edit(Long id, MemberEdit memberEdit) {

        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        MemberEditor.MemberEditorBuilder editorBuilder = member.toEditor();

        MemberEditor memberEditor = editorBuilder
                .nickname(memberEdit.getNickname())
                .build();

        if (memberRepository.existsByNickname(memberEdit.getNickname())) {
            throw new MemberNicknameDuplicationException("nickname", "?????? ???????????? ??????????????????.");
        } else {
            member.edit(memberEditor);
        }
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
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();

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
            throw new PasswordNotMatchException("password", "??????????????? ???????????? ????????????.");
        }
    }
}
