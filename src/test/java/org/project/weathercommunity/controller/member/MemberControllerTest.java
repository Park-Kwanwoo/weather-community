package org.project.weathercommunity.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.weathercommunity.config.security.token.JwtTokenProvider;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.member.Role;
import org.project.weathercommunity.domain.token.Token;
import org.project.weathercommunity.exception.TokenNotFoundException;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.repository.token.TokenRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class MemberControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("???????????? ???????????? ?????????")
    void JOIN_MEMBER() throws Exception {

        // given
        MemberCreate request = MemberCreate.builder()
                .email("test@never.com")
                .nickname("?????????")
                .password("tester12#")
                .build();

        String jsonData = objectMapper.writeValueAsString(request);
        String encodedPassword = passwordEncoder.encode("tester12#");

        // when
        mockMvc.perform(post("/members/join")
                        .contentType(APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, memberRepository.count());
        Member member = memberRepository.findAll().get(0);
        assertAll(
                () -> assertNotEquals("tester12#", passwordEncoder.encode("tester12#")),
                () -> assertTrue(passwordEncoder.matches("tester12#", encodedPassword))
        );
        assertEquals("test@never.com", member.getEmail());
        assertEquals("?????????", member.getNickname());
        assertEquals(Role.ROLE_USER, member.getRole());
    }

    @Test
    @DisplayName("?????? ?????? ??? ????????? ????????? ?????? ????????? ????????? ??????")
    void VALID_MEMBER_EMAIL() throws Exception {

        // given
        MemberCreate member = MemberCreate.builder()
//                .email("test@case!com")
                .email("@test.com")
//                .email("test@never.com")
                .nickname("?????????")
                .password("tester12#")
                .build();

        String jsonValue = objectMapper.writeValueAsString(member);

        // expected
        mockMvc.perform(post("/members/join")
                        .contentType(APPLICATION_JSON)
                        .content(jsonValue))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("????????? ???????????????."))
                .andExpect(jsonPath("$.validation.email").value("???????????? ????????? ???????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ??? ???????????? ????????? ?????? ????????? ????????? ??????")
    void VALID_MEMBER_PASSWORD() throws Exception {

        // given
        MemberCreate member = MemberCreate.builder()
                .email("test@never.com")
                .nickname("?????????")
//                .password("tester12")
                .password("test12#")
                .build();

        String jsonValue = objectMapper.writeValueAsString(member);

        // expected
        mockMvc.perform(post("/members/join")
                        .contentType(APPLICATION_JSON)
                        .content(jsonValue))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("????????? ???????????????."))
                .andExpect(jsonPath("$.validation.password").value("??????, ??????, ???????????? ?????? 8~15?????? ????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void MEMBER_EDIT_TEST() throws Exception {

        // given
        Member member = Member.builder()
                .email("test@never.com")
                .nickname("?????????")
                .password("tester12#")
                .build();

        memberRepository.save(member);

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        System.out.println(accessToken);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();

        tokenRepository.save(token);

        Token savedToken = tokenRepository.findByMember(member).orElseThrow(TokenNotFoundException::new);

        // when
        MemberEdit memberEdit = MemberEdit.builder()
                .nickname("????????????")
                .build();

        String jsonValue = objectMapper.writeValueAsString(memberEdit);

        // expected
        mockMvc.perform(patch("/members/{memberId}", member.getId())
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(jsonValue))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("?????? ?????? ?????????")
    void MEMBER_DELETE() throws Exception {

        // given
        Member member = Member.builder()
                .email("test@never.com")
                .nickname("?????????")
                .password("tester12#")
                .build();

        memberRepository.save(member);

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();

        tokenRepository.save(token);

        Token savedToken = tokenRepository.findByMember(member).orElseThrow(TokenNotFoundException::new);


        // expected
        mockMvc.perform(delete("/members/{memberId}", member.getId())
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}