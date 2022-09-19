package org.project.weathercommunity.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.weathercommunity.config.security.token.JwtAuthenticationToken;
import org.project.weathercommunity.exception.JwtExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtTokenTest {

    @Autowired
    UserDetailsService userDetailsService;

    private final String key = "aGVsbG8tbXktcmVhbC1uYW1lLWlzLWt3YW53b28tdGhpcy1rZXktaXMtdmVyeS1pbXBvcnRhbnQtc28tYmUtY2FyZWZ1bC10aGFuay15b3U=";

    @Test
    @DisplayName("토큰 발행 테스트")
    void 토큰_발행() {

        // given
        JwtAuthenticationToken jwtAuthenticationToken =
                new JwtAuthenticationToken(key, 1800000, 604800000, userDetailsService);

        String email = "test@case.com";

        // when
        String accessToken = jwtAuthenticationToken.createAccessToken(email);
        Claims claims = jwtAuthenticationToken.parseClaims(accessToken);

        // then
        assertEquals(email, claims.getSubject());

    }

    @Test
    @DisplayName("토큰 만료 시간 테스트")
    void 토큰_만료() throws InterruptedException {

        // given
        JwtAuthenticationToken jwtAuthenticationToken =
                new JwtAuthenticationToken(key, 1, 10, userDetailsService);

        String email = "test@case.com";
        String accessToken = jwtAuthenticationToken.createAccessToken(email);


        // when
        Thread.sleep(60);

        // then
        assertThrows(JwtExpiredException.class, () -> {
            jwtAuthenticationToken.parseClaims(accessToken);
        });



    }
}
