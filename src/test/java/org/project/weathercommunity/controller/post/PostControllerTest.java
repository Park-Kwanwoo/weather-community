package org.project.weathercommunity.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.weathercommunity.config.security.token.JwtTokenProvider;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.post.Post;
import org.project.weathercommunity.domain.token.Token;
import org.project.weathercommunity.exception.TokenNotFoundException;
import org.project.weathercommunity.repository.member.MemberRepository;
import org.project.weathercommunity.repository.post.PostRepository;
import org.project.weathercommunity.repository.token.TokenRepository;
import org.project.weathercommunity.request.post.PostCreate;
import org.project.weathercommunity.request.post.PostEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    private Member saveMember() {
        Member member = new Member("test@case.com", "qwer123$", "?????????", "010-1111-1111");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    void POST_WRITE() throws Exception {

        Member member = saveMember();

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();

        tokenRepository.save(token);

        Token savedToken = tokenRepository.findByMember(member).orElseThrow(TokenNotFoundException::new);

        //given
        PostCreate request = PostCreate.builder()
                .title("??????")
                .content("??????")
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts/create")
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? ??? ????????? ??????")
    void POST_TITLE_MUST_NOT_NULL() throws Exception {

        //given
        Member member = saveMember();

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();

        tokenRepository.save(token);

        Token savedToken = tokenRepository.findByMember(member).orElseThrow(TokenNotFoundException::new);


        PostCreate request = PostCreate.builder()
                .title(null)
                .content("??????")
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts/create")
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("????????? ???????????????."))
                .andExpect(jsonPath("$.validation.title").value("???????????? ??????????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? ??? ????????? ??????")
    void POST_CONTENT_MUST_NOT_NULL() throws Exception {

        //given
        Member member = saveMember();

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();

        tokenRepository.save(token);

        Token savedToken = tokenRepository.findByMember(member).orElseThrow(TokenNotFoundException::new);


        PostCreate request = PostCreate.builder()
                .title("??????")
                .content(null)
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts/create")
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("????????? ???????????????."))
                .andExpect(jsonPath("$.validation.content").value("???????????? ???????????????."))
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? ??? DB??? ??? ??????")
    void DB_SAVE_TEST_WHEN_REQUEST_POST() throws Exception {

        // given
        Member member = saveMember();

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();

        tokenRepository.save(token);

        Token savedToken = tokenRepository.findByMember(member).orElseThrow(TokenNotFoundException::new);


        PostCreate request = PostCreate.builder()
                .title("??????")
                .content("??????")
                .build();

        String requestToJson = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts/create")
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("??????", post.getTitle());
        assertEquals("??????", post.getContent());
    }

    @Test
    @DisplayName("??? 1??? ?????? Controller ?????????")
    void GET_POST_ONE() throws Exception {

        // given
        Member member = saveMember();

        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .member(member)
                .build();

        postRepository.save(post);

        // expected (when??? then??? ?????? ??????)
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("??? ?????? ?????? Controller ?????????")
    void GET_POST_LIST() throws Exception {

        // given
        Member member = saveMember();

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("??????" + i)
                            .content("??????" + i)
                            .member(member)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);


        // expected (when??? then??? ?????? ??????)
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
//                .andExpect(jsonPath("$[0].id").value(num))
                .andExpect(jsonPath("$[0].title").value("??????" + 30))
                .andExpect(jsonPath("$[0].content").value("??????" + 30))
                .andDo(print());
    }

    @Test
    @DisplayName("??? ?????? ?????? Controller ?????????")
    void EDIT_POST_TITLE() throws Exception {

        // given
        Member member = saveMember();

        Post post = Post.builder()
                .title("?????????")
                .content("??????")
                .member(member)
                .build();

        postRepository.save(post);

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();

        tokenRepository.save(token);

        Token savedToken = tokenRepository.findByMember(member).orElseThrow(TokenNotFoundException::new);

        PostEdit postEdit = PostEdit.builder()
                .title("?????????")
                .content("??????")
                .build();

        // expected (when??? then??? ?????? ??????)
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? Controller ?????????")
    void DELETE_POST() throws Exception {

        // given
        Member member = saveMember();

        Post post = Post.builder()
                .title("?????????")
                .content("??????")
                .member(member)
                .build();

        postRepository.save(post);

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
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ??????")
    void THROW_NOTFOUND_WHEN_FIND_NOT_EXIST_POST() throws Exception {

        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("???????????? ?????? ????????? ??????")
    void THROW_NOTFOUND_WHEN_EDIT_NOT_EXIST_POST() throws Exception {

        // given
        Member member = saveMember();

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();

        tokenRepository.save(token);

        Token savedToken = tokenRepository.findByMember(member).orElseThrow(TokenNotFoundException::new);

        PostEdit postEdit = PostEdit.builder()
                .title("?????????")
                .content("??????")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ?????? ??? ????????? '??????'??? ????????? ??? ??????.")
    void WHEN_WIRTE_POST_SPEICIFIC_WORD_IS_NOT_PERMITED() throws Exception {

        // given
        Member member = saveMember();

        Post request = Post.builder()
                .title("?????????")
                .content("??????")
                .member(member)
                .build();

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();

        tokenRepository.save(token);

        Token savedToken = tokenRepository.findByMember(member).orElseThrow(TokenNotFoundException::new);

        String requestToJson = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts/create")
                        .header(AUTHORIZATION, savedToken.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        .content(requestToJson)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}