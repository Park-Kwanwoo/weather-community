package org.project.weathercommunity.controller.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.request.post.PostCreate;
import org.project.weathercommunity.request.post.PostEdit;
import org.project.weathercommunity.request.post.PostSearch;
import org.project.weathercommunity.response.post.PostListResponse;
import org.project.weathercommunity.response.post.PostOneResponse;
import org.project.weathercommunity.service.post.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts/create")
    public void post(@RequestBody @Valid PostCreate postCreate, @AuthenticationPrincipal Member member) {
        // Case1. 저장한 데이터 Entity -> response로 응답하기
        // Case2. 저장한 데이터의 primary_id -> response로 응답하기
        //          Client에서는 수신한 id를 글 조회 API를 통해서 데이터를 수신받음
        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context 잘 관리함
        // Bad Case: 서버에서 -> 반드시 이렇게 한다 (fix)
        //          -> 서버에서 차라리 유연하게 대응하는게 좋다. -> 코드를 잘 짜야함.
        //          -> 한 번에 일괄적으로 잘 처리되는 케이스는 X -> 잘 관리하는 형태가 중요
        postCreate.validate();
        postService.write(postCreate, member);
    }

    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */

    @GetMapping("/posts/{postId}")
    public PostOneResponse get(@PathVariable(name = "postId") Long id) {
        return postService.get(id);
    }

    @GetMapping("/posts")
    public List<PostListResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable(value = "postId") Long id, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(id, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable(value = "postId") Long id) {
        postService.delete(id);
    }

    @GetMapping("/posts/totalPage")
    public long totalPage() {
        return postService.totalPage();
    }
}
