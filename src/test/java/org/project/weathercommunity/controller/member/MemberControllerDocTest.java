package org.project.weathercommunity.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.member.MemberEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

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
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 등록")
    void 회원_등록_Doc() throws Exception {

        // given
        MemberCreate request = MemberCreate.builder()
                .email("test@case.com")
                .name("테스터")
                .phone("010-2314-1232")
                .password("tester12#")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/members/join")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-join",
                        requestFields(
                                fieldWithPath("email").description("제목"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("role").description("회원 등급")

                        )
                ));
    }

    @Test
    @DisplayName("회원 수정_doc")
    void 회원_수정() throws Exception {

        // given
        Member member = Member.builder()
                .email("test@case.com")
                .name("테스터")
                .phone("010-2314-1232")
                .password("tester12#")
                .build();

        memberRepository.save(member);

        MemberEdit memberEdit = MemberEdit.builder()
                .name("테스터")
                .password("tester34#")
                .phone("010-1111-1111")
                .build();

        String jsonValue = objectMapper.writeValueAsString(memberEdit);

        // expected
        mockMvc.perform(patch("/members/{memberId}", member.getId())
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
                                        fieldWithPath("password").description("비밀번호"),
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("phone").description("전화번호")
                                )
                        )
                );
    }

    @Test
    @DisplayName("회원 삭제_doc")
    void 회원_삭제() throws Exception {

        // given
        Member member = Member.builder()
                .email("test@case.com")
                .name("테스터")
                .phone("010-2314-1232")
                .password("tester12#")
                .build();

        memberRepository.save(member);

        // expected
        mockMvc.perform(delete("/members/{memberId}", member.getId())
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
