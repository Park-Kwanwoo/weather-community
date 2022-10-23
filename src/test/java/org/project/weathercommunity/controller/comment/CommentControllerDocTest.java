package org.project.weathercommunity.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.weathercommunity.config.security.token.JwtTokenProvider;
import org.project.weathercommunity.domain.comment.Comment;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.post.Post;
import org.project.weathercommunity.domain.token.Token;
import org.project.weathercommunity.repository.comment.CommentRepository;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.repository.post.PostRepository;
import org.project.weathercommunity.repository.token.TokenRepository;
import org.project.weathercommunity.request.comment.CommentCreate;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.weathercommunity.com", uriPort = 443)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@ExtendWith(RestDocumentationExtension.class)
public class CommentControllerDocTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Member saveMember() {
        Member member = new Member("test@case.com", "qwer123$", "아무개", "010-1111-1111");
        return memberRepository.save(member);
    }

    private Post savePost(Member member) {
        Post post = new Post("제목", "내용", member);
        return postRepository.save(post);
    }

    private CommentCreate createComment(Post post) {
        return CommentCreate.builder()
                .content("댓글")
                .postId(post.getId())
                .build();
    }

    private Comment toComment(CommentCreate commentCreate, Post post, Member member) {
        return Comment.builder()
                .content("댓글 내용")
                .post(post)
                .member(member)
                .build();
    }

    private Token saveToken(Member member) {

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());
        Token token = new Token(accessToken, refreshToken, member);
        return tokenRepository.save(token);
    }

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
        tokenRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 등록 DOCUMENT")
    void WRITE_COMMENT() throws Exception {

        // given
        Member member = saveMember();
        Post post = savePost(member);
        Token token = saveToken(member);
        CommentCreate commentCreate = createComment(post);

        String requestToJson = objectMapper.writeValueAsString(commentCreate);

        // expected
        mockMvc.perform(post("/comments/create")
                        .header(AUTHORIZATION, token.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isOk())
                .andDo(document("comment-create",
                        requestFields(
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("postId").description("게시물 번호")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 삭제 DOCUMENT")
    void DELETE_COMMENT() throws Exception {

        // given
        Member member = saveMember();
        Post post = savePost(member);
        Token token = saveToken(member);
        CommentCreate commentCreate = createComment(post);
        Comment comment = toComment(commentCreate, post, member);

        commentRepository.save(comment);

        // expected
        mockMvc.perform(delete("/comments/{commentId}", comment.getId())
                        .header(AUTHORIZATION, token.getAccessToken())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("comment-delete", pathParameters(
                        parameterWithName("commentId").description("댓글 번호")
                        )));
    }
}