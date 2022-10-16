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
    @DisplayName("회원가입 컨트롤러 테스트")
    void JOIN_MEMBER() throws Exception {

        // given
        MemberCreate request = MemberCreate.builder()
                .email("test@never.com")
                .nickname("테스터")
                .phone("010-1234-5678")
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
        assertEquals("테스터", member.getNickname());
        assertEquals("010-1234-5678", member.getPhone());
        assertEquals(Role.ROLE_USER, member.getRole());
    }

    @Test
    @DisplayName("회원 가입 시 이메일 양식에 맞지 않으면 오류를 리턴")
    void VALID_MEMBER_EMAIL() throws Exception {

        // given
        MemberCreate member = MemberCreate.builder()
//                .email("test@case!com")
                .email("@test.com")
//                .email("test@never.com")
                .nickname("테스터")
                .phone("010-1234-5678")
                .password("tester12#")
                .build();

        String jsonValue = objectMapper.writeValueAsString(member);

        // expected
        mockMvc.perform(post("/members/join")
                        .contentType(APPLICATION_JSON)
                        .content(jsonValue))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.email").value("제대로된 이메일 형식으로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 시 전화번호 양식에 맞지 않으면 오류를 리턴")
    void VALID_MEMBER_PHONE() throws Exception {

        // given
        MemberCreate member = MemberCreate.builder()
                .email("test@never.com")
                .nickname("테스터")
//                .phone("02-1234-5678")
//                .phone("010-1234-523678")
                .phone("010-21234-5678")
                .password("tester12#")
                .build();

        String jsonValue = objectMapper.writeValueAsString(member);

        // expected
        mockMvc.perform(post("/members/join")
                        .contentType(APPLICATION_JSON)
                        .content(jsonValue))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.phone").value("전화번호 형식에 맞게 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 시 비밀번호 양식에 맞지 않으면 오류를 리턴")
    void VALID_MEMBER_PASSWORD() throws Exception {

        // given
        MemberCreate member = MemberCreate.builder()
                .email("test@never.com")
                .nickname("테스터")
                .phone("010-2124-5678")
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
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.password").value("숫자, 문자, 특수문자 포함 8~15자리 이내로 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 수정 테스트")
    void MEMBER_EDIT_TEST() throws Exception {

        // given
        Member member = Member.builder()
                .email("test@never.com")
                .nickname("테스터")
                .phone("010-1234-5678")
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
                .nickname("테스터훈")
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
    @DisplayName("회원 삭제 테스트")
    void MEMBER_DELETE() throws Exception {

        // given
        Member member = Member.builder()
                .email("test@never.com")
                .nickname("테스터")
                .phone("010-1234-5678")
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