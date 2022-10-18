package org.project.weathercommunity.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.weathercommunity.config.security.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JwtTokenProviderTest {

    @Autowired
    UserDetailsService userDetailsService;

    private final String key = "aGVsbG8tbXktcmVhbC1uYW1lLWlzLWt3YW53b28tdGhpcy1rZXktaXMtdmVyeS1pbXBvcnRhbnQtc28tYmUtY2FyZWZ1bC10aGFuay15b3U=";

    @Test
    @DisplayName("토큰 발행 테스트")
    void CREATE_TOKEN() {

        // given
        JwtTokenProvider jwtTokenProvider =
                new JwtTokenProvider(key, 1800000, 604800000, userDetailsService);

        String email = "test@case.com";

        // when
        String accessToken = jwtTokenProvider.createAccessToken(email).substring(7);

        //then
        assertEquals("test@case.com", jwtTokenProvider.getMemberEmailByToken(accessToken));

    }

    @Test
    @DisplayName("토큰 만료 시간 테스트")
    void EXPIRE_TOKEN() throws InterruptedException {

        // given
        JwtTokenProvider jwtTokenProvider =
                new JwtTokenProvider(key, 1, 10, userDetailsService);

        String email = "test@case.com";
        String accessToken = jwtTokenProvider.createAccessToken(email);

        // when
        Thread.sleep(60);

        // then
        assertFalse(jwtTokenProvider.validTokenExpired(accessToken));

    }
}
