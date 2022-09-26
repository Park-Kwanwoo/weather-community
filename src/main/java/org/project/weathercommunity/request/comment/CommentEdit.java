package org.project.weathercommunity.request.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CommentEdit {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public CommentEdit(String content) {
        this.content = content;
    }
}
