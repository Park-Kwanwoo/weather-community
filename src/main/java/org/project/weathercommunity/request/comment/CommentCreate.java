package org.project.weathercommunity.request.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentCreate {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "글 번호가 존재하지 않습니다.")
    private Long postId;

    @Builder
    public CommentCreate(String content, Long postId) {
        this.content = content;
        this.postId = postId;
    }
}
