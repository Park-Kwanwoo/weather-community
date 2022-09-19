package org.project.weathercommunity.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.MemberEditor;
import org.project.weathercommunity.exception.InvalidRequest;
import org.project.weathercommunity.exception.MemberNotFound;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.project.weathercommunity.response.member.MemberMypageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
                .orElseThrow(MemberNotFound::new);

        MemberEditor.MemberEditorBuilder editorBuilder = member.toEditor();

        MemberEditor memberEditor = editorBuilder
                .name(memberEdit.getName())
//                .password(memberEdit.getPassword())
                .phone(memberEdit.getPhone())
                .build();


        member.edit(memberEditor);
    }

    public void delete(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);

        memberRepository.delete(member);
    }

    public MemberMypageResponse get(Long id, Authentication authentication) {

        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);

        Member currentMember = (Member) authentication.getPrincipal();

        if (member.getId().equals(currentMember.getId())) {
            return MemberMypageResponse.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .name(member.getName())
                    .phone(member.getPhone())
                    .build();
        } else {
            throw  new InvalidRequest("auth", "잘못된 접근입니다.");
        }
    }

    public boolean duplicateCheck(String email) {
        return memberRepository.existsByEmail(email);
    }
}
