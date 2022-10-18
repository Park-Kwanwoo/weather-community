package org.project.weathercommunity.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.weathercommunity.config.security.token.JwtTokenProvider;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.token.Token;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.repository.token.TokenRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.weathercommunity.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class MemberControllerDocTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    private MemberCreate memberCreate() {
        return MemberCreate.builder() .email("test@case.com")
                .nickname("테스터")
                .phone("010-2314-1232")
                .password("tester12#")
                .build();
    }

    private Member member(MemberCreate memberCreate) {
        return Member.builder()
                .email(memberCreate.getEmail())
                .nickname(memberCreate.getNickname())
                .phone(memberCreate.getPhone())
                .password(passwordEncoder.encode(memberCreate().getPassword()))
                .build();
    }


    private Token createToken(Member member) {
        return Token.builder()
                .accessToken(jwtTokenProvider.createAccessToken(member.getEmail()))
                .refreshToken(jwtTokenProvider.createRefreshToken(member.getEmail()))
                .member(member)
                .build();
    }

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 등록 DOCUMENT")
    void MEMBER_JOIN_DOCUMENT() throws Exception {

        // given
        MemberCreate memberCreate = memberCreate();

        String json = objectMapper.writeValueAsString(memberCreate);

        // expected
        mockMvc.perform(post("/members/join")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-join",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("role").description("회원 등급")

                        )
                ));
    }

    @Test
    @DisplayName("회원 수정 DOCUMENT")
    void MEMBER_EDIT_DOCUMENT() throws Exception {

        // given
        MemberCreate memberCreate = memberCreate();
        Member member = member(memberCreate);

        memberRepository.save(member);

        Token token = createToken(member);

        MemberEdit memberEdit = MemberEdit.builder()
                .nickname("닉네임")
                .build();

        Token savedToken = tokenRepository.save(token);

        String jsonValue = objectMapper.writeValueAsString(memberEdit);

        // expected
        mockMvc.perform(patch("/members/{memberId}", member.getId())
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(jsonValue)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("member-edit",
                                pathParameters(
                                        parameterWithName("memberId").description("회원 ID")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").description("닉네임")
                                )
                        )
                );
    }

    @Test
    @DisplayName("회원 삭제 DOCUMENT")
    void MEMBER_DELETE_DOCUMENT() throws Exception {

        // given
        MemberCreate memberCreate = memberCreate();
        Member member = member(memberCreate);

        memberRepository.save(member);

        Token token = createToken(member);
        Token savedToken = tokenRepository.save(token);

        // expected
        mockMvc.perform(delete("/members/{memberId}", member.getId())
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("member-delete",
                                pathParameters(
                                        parameterWithName("memberId").description("회원 ID")
                                )
                        )
                );
    }
}
