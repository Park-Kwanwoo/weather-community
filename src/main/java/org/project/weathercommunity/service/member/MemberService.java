package org.project.weathercommunity.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.MemberEditor;
import org.project.weathercommunity.exception.MemberNotFoundException;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.project.weathercommunity.response.member.MemberMypageResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
//                .password(memberEdit.getPassword())
                .phone(memberEdit.getPhone())
                .build();


        member.edit(memberEditor);
    }

    public void delete(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        memberRepository.delete(member);
    }

    public MemberMypageResponse get(String email) {

        Member member = memberRepository.findByEmail(email)
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
}
