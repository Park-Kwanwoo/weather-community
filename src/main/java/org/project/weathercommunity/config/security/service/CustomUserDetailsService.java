package org.project.weathercommunity.config.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.exception.MemberNotFound;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("userDetailService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws MemberNotFound {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFound::new);
        return new CustomUserDetails(member);
    }
}
