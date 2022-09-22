package org.project.weathercommunity.config.security.service;

import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.exception.MemberNotFoundException;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service("userDetailService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws MemberNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        return new CustomUserDetails(member);
    }
}
