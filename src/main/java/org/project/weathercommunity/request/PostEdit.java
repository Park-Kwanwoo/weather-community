package org.project.weathercommunity.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.project.weathercommunity.domain.Post;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class PostEdit {

    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
