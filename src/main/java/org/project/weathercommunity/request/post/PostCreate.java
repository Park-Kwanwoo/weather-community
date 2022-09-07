package org.project.weathercommunity.request.post;

import lombok.*;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.exception.InvalidRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "콘텐츠를 입력하세요.")
    private String content;

    @NotNull
    private Member member;

    @Builder
    public PostCreate(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }
}
