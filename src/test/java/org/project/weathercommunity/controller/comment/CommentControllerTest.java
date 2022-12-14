package org.project.weathercommunity.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public class CommentControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    MockMvc mockMvc;


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        tokenRepository.deleteAll();
        commentRepository.deleteAll();
    }

    private Member saveMember() {
        Member member = new Member("test@case.com", "qwer123$", "?????????", "");
        return memberRepository.save(member);
    }

    private Post savePost(Member member) {
        Post post = new Post("??????", "??????", member);
        return postRepository.save(post);
    }

    private Token saveToken(Member member) {

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());
        Token token = new Token(accessToken, refreshToken, member);
        return tokenRepository.save(token);
    }

    @Test
    @DisplayName("?????? ??????")
    void SAVE_COMMENT() throws Exception {

        // given
        Member member = saveMember();
        Post post = savePost(member);

        Long postId = post.getId();

        CommentCreate request = CommentCreate.builder()
                .content("??????")
                .postId(postId)
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        Token token = saveToken(member);

        // expected
        mockMvc.perform(post("/comments/create")
                        .header(AUTHORIZATION, token.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ??? ?????? ??????")
    void COMMENT_CONTENT_MUST_NOT_NULL() throws Exception {

        // given
        Member member = saveMember();
        Post post = savePost(member);

        Long postId = post.getId();

        CommentCreate request = CommentCreate.builder()
                .content(null)
                .postId(postId)
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        Token token = saveToken(member);

        // expected
        mockMvc.perform(post("/comments/create")
                        .header(AUTHORIZATION, token.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("????????? ???????????????."))
                .andExpect(jsonPath("$.validation.content").value("????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ??? ???????????? ?????? ??????????????? PostNotFoundException ??????")
    void THROW_POST_NOT_FOUND_EXCEPTION_WHEN_WRITE_COMMENT() throws Exception {

        // given
        Member member = saveMember();

        CommentCreate request = CommentCreate.builder()
                .content("??????")
                .postId(2L)
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        Token token = saveToken(member);

        // expected
        mockMvc.perform(post("/comments/create")
                        .header(AUTHORIZATION, token.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("???????????? ?????? ????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("?????? ????????? ????????????")
    void GET_COMMENT_LIST() throws Exception {

        // given
        Member member = saveMember();
        Post post = savePost(member);

        List<Comment> requestComments = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Comment.builder()
                            .content("??????" + i)
                            .post(post)
                            .member(member)
                            .build();
                })
                .collect(Collectors.toList());

        commentRepository.saveAll(requestComments);

        // expected
        mockMvc.perform(get("/comments?postId=" + post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(30)))
                .andExpect(jsonPath("$[0].content").value("??????1"));
    }

    @Test
    @DisplayName("?????? ??????")
    void COMMENT_DELETE() throws Exception {

        // given
        Member member = saveMember();
        Post post = savePost(member);

        Comment comment = Comment.builder()
                .content("??????")
                .post(post)
                .member(member)
                .build();
        commentRepository.save(comment);

        Token token = saveToken(member);


        // expected
        mockMvc.perform(delete("/comments/{commentId}", comment.getId())
                        .header(AUTHORIZATION, token.getAccessToken())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
