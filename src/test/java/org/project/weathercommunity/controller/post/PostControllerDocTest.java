package org.project.weathercommunity.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.weathercommunity.config.security.token.JwtTokenProvider;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.post.Post;
import org.project.weathercommunity.domain.token.Token;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.repository.post.PostRepository;
import org.project.weathercommunity.repository.token.TokenRepository;
import org.project.weathercommunity.request.member.MemberCreate;
import org.project.weathercommunity.request.post.PostCreate;
import org.project.weathercommunity.request.post.PostEdit;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.weathercommunity.com", uriPort = 443)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

// API 문서 생성

// GET /posts/{postId} -> 단건 조회
// POST /posts -> 게시글 등록

// 클라이언트 입장 어떤 API 있는지 모름

// Spring RestDocs
// Test 케이스 실행 -> 문서를 생성

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    ObjectMapper objectMapper;

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

    private Member toMember(MemberCreate memberCreate) {
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

    private PostCreate postCreate() {
        return PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();
    }

    private Post toPost(PostCreate postCreate, Member member) {
        return Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .member(member)
                .build();
    }

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
        tokenRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 단건 조회 DOCUMENT")
    void GET_POST_ONE_DOCUMENT() throws Exception {

        // given
        MemberCreate memberCreate = memberCreate();
        Member member = toMember(memberCreate);

        memberRepository.save(member);

        PostCreate postCreate = postCreate();
        Post post = toPost(postCreate, member);
        postRepository.save(post);

        // expected
        this.mockMvc.perform(get("/posts/{postId}", post.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry", pathParameters(
                        parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("createdTime").description("작성일"),
                                fieldWithPath("memberId").description("회원 ID")
                        )

                ));
    }

    @Test
    @DisplayName("글 등록 DOCUMENT")
    void WRITE_POST_DOCUMENT() throws Exception {

        // given
        MemberCreate memberCreate = memberCreate();
        Member member = toMember(memberCreate);
        memberRepository.save(member);

        Token token = createToken(member);
        Token savedToken = tokenRepository.save(token);

        PostCreate postCreate = postCreate();

        String json = objectMapper.writeValueAsString(postCreate);


        // expected
        this.mockMvc.perform(post("/posts/create")
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 수정 DOCUMENT")
    void EDIT_POST_DOCUMENT() throws Exception {

        // given
        MemberCreate memberCreate = memberCreate();
        Member member = toMember(memberCreate);
        memberRepository.save(member);

        Token token = createToken(member);
        Token savedToken = tokenRepository.save(token);

        PostCreate postCreate = postCreate();
        Post post = toPost(postCreate, member);

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목 수정")
                .content("내용 수정")
                .build();

        // expectd
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-edit", pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 삭제 DOCUMENT")
    void DELETE_POST_DOCUMENT() throws Exception {

        // given
        MemberCreate memberCreate = memberCreate();
        Member member = toMember(memberCreate);
        memberRepository.save(member);

        Token token = createToken(member);
        Token savedToken = tokenRepository.save(token);

        PostCreate postCreate = postCreate();
        Post post = toPost(postCreate, member);

        postRepository.save(post);


        postRepository.save(post);

        // expectd
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-delete", pathParameters(
                        parameterWithName("postId").description("게시글 번호")
                )));
    }
}
