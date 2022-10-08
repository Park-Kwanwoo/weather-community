package org.project.weathercommunity.config.security.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.config.security.service.CustomUserDetails;
import org.project.weathercommunity.config.security.token.VueAuthenticationToken;
import org.project.weathercommunity.exception.MemberNotFoundAuthenticationException;
import org.project.weathercommunity.exception.MemberNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VueAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // 검증을 위한 로직
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = (String) authentication.getCredentials();
        try {
            CustomUserDetails member = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

            if (!passwordEncoder.matches(password, member.getMember().getPassword())) {
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }

            return new VueAuthenticationToken(member.getMember(), null,  member.getAuthorities());
        } catch (MemberNotFoundException e) {
            throw new MemberNotFoundAuthenticationException("존재하지않는 이메일입니다.");
        }

    }

    // 파라미터로 전달되는 토큰의 타입 일치 여부에 따른 리턴
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(VueAuthenticationToken.class);
    }
}
