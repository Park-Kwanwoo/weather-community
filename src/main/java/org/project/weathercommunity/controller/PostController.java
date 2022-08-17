package org.project.weathercommunity.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.domain.Post;
import org.project.weathercommunity.request.PostCreate;
import org.project.weathercommunity.service.PostService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate postCreate) {
        // Case1. 저장한 데이터 Entity -> response로 응답하기
        // Case2. 저장한 데이터의 primary_id -> response로 응답하기
        //          Client에서는 수신한 id를 글 조회 API를 통해서 데이터를 수신받음
        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context 잘 관리함
        // Bad Case: 서버에서 -> 반드시 이렇게 한다 (fix)
        //          -> 서버에서 차라리 유연하게 대응하는게 좋다. -> 코드를 잘 짜야함.
        //          -> 한 번에 일괄적으로 잘 처리되는 케이스는 X -> 잘 관리하는 형태가 중요
        postService.write(postCreate);
    }

    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */

    @GetMapping("/posts/{postId}")
    public Post get(@PathVariable(name = "postId") Long id) {
        return postService.get(id);
    }
}
