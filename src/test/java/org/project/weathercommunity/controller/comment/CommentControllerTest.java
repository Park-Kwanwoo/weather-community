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
        Member member = new Member("test@case.com", "qwer123$", "아무개", "010-1111-1111");
        return memberRepository.save(member);
    }

    private Post savePost(Member member) {
        Post post = new Post("제목", "내용", member);
        return postRepository.save(post);
    }

    private Token saveToken(Member member) {

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());
        Token token = new Token(accessToken, refreshToken, member);
        return tokenRepository.save(token);
    }

    @Test
    @DisplayName("댓글 저장")
    void SAVE_COMMENT() throws Exception {

        // given
        Member member = saveMember();
        Post post = savePost(member);

        Long postId = post.getId();

        CommentCreate request = CommentCreate.builder()
                .content("댓글")
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
    @DisplayName("댓글 작성 시 내용 필수")
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
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.content").value("내용을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 작성 시 존재하지 않는 게시글이면 PostNotFoundException 리턴")
    void THROW_POST_NOT_FOUND_EXCEPTION_WHEN_WRITE_COMMENT() throws Exception {

        // given
        Member member = saveMember();

        CommentCreate request = CommentCreate.builder()
                .content("내용")
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
                .andExpect(jsonPath("$.message").value("존재하지 않는 글입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 리스트 가져오기")
    void GET_COMMENT_LIST() throws Exception {

        // given
        Member member = saveMember();
        Post post = savePost(member);

        List<Comment> requestComments = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Comment.builder()
                            .content("댓글" + i)
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
                .andExpect(jsonPath("$[0].content").value("댓글1"));
    }

    @Test
    @DisplayName("댓글 삭제")
    void COMMENT_DELETE() throws Exception {

        // given
        Member member = saveMember();
        Post post = savePost(member);

        Comment comment = Comment.builder()
                .content("내용")
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
