package org.project.weathercommunity.controller.comment;

import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.request.comment.CommentCreate;
import org.project.weathercommunity.request.comment.CommentEdit;
import org.project.weathercommunity.response.comment.CommentResponse;
import org.project.weathercommunity.service.comment.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/create")
    public void comment(@RequestBody @Valid CommentCreate commentCreate, @AuthenticationPrincipal Member member) {
        commentService.write(commentCreate, member);
    }

    @GetMapping("/comments")
    public List<CommentResponse> getList(@RequestParam Long postId) {
        return commentService.getList(postId);
    }

    @PatchMapping("/comments/{commentId}")
    public void edit(@PathVariable("commentId") Long id, @RequestBody @Valid CommentEdit commentEdit) {
        commentService.edit(id, commentEdit);
    }

    @DeleteMapping("/comments/{commentId}")
    public void delete(@PathVariable("commentId") Long id) {
        commentService.delete(id);
    }
}
