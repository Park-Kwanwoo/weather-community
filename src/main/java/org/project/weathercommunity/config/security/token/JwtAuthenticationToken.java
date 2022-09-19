package org.project.weathercommunity.config.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.domain.member.Role;
import org.project.weathercommunity.exception.JwtExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Component
public class JwtAuthenticationToken {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final UserDetailsService userDetailsService;

    private final String key;

    public JwtAuthenticationToken(@Value("${jwt.secret}") String secretKey,
                                  @Value("${jwt.access-token-expire-time}") long accessTime,
                                  @Value("${jwt.refresh-token-expire-time}") long refreshTime,
                                  UserDetailsService userDetailsService) {
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;
        this.userDetailsService = userDetailsService;
        this.key = secretKey;

    }

    protected String createToken(String email, long tokenValid) {

        log.info("createToken");
        // ex) sub: jwt@jwt.com
         /*
         payload에 해당하는 부분

         1. Registered claims: 미리 정의된 클레임
            iss : 발행자
            exp : 만료 시간
            iat : 발행 시간
            sub : 제목
            jti : JWI ID
         2. Public claims: 사용자가 정의할 수 있는 클레임 공개용 정보 전달을 위해 사용
         3. Private claims: 해당하는 당사자들 간에 정보를 공유하기 위해 만들어진 사용자 지정 클레임.
                            외부에 공개되도 상관없지만 해당 유저를 특정할 수 있는 정ㅂ들을 담는다.

         */
        Claims claims = Jwts.claims()
                .setSubject(email);

        claims.put(AUTHORITIES_KEY, Role.ROLE_USER);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)    // 토큰 발행 유저 정보
                .setIssuedAt(now)     // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + tokenValid))  // 토큰 만료시간
                .signWith(SignatureAlgorithm.HS512, key)              // 키, 알고리즘 설정
                .compact();
    }

    public String createAccessToken(String email) {
        return this.createToken(email, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createRefreshToken(String email) {
        return this.createToken(email, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String getMemberEmailByToken(String token) {
        // 토큰의 claim의 sub 키에 이메일 값이 들어있다.
        return this.parseClaims(token).getSubject();
    }

    public Authentication getAuthentication(String accessToken) {

        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        log.info("subject = {}", claims.getSubject());

        if (claims.get(AUTHORITIES_KEY) == null || !StringUtils.hasText(claims.get(AUTHORITIES_KEY).toString())) {
            throw new IllegalArgumentException();
        }

        Authentication authentication = (Authentication) userDetailsService.loadUserByUsername(getMemberEmailByToken(accessToken));

        log.info("principal = {}", authentication.getPrincipal());
        log.info("authorities = {}", authentication.getAuthorities());

        return new VueAuthenticationToken(authentication.getPrincipal(), "", authentication.getAuthorities());
    }

    public Claims parseClaims(String accessToken) {

        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {   // 만료된 토큰이 더라도 일단 파싱을 함
            throw new JwtExpiredException("토큰 유효시간 만료입니다.");
        }
    }
}
