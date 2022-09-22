package org.project.weathercommunity.domain.token;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.weathercommunity.domain.member.Member;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Token(Long id, String accessToken, String refreshToken, Member member) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public TokenEditor.TokenEditorBuilder toEditor() {
        return TokenEditor.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken);
    }

    public void edit(TokenEditor tokenEditor) {
        accessToken = tokenEditor.getAccessToken();
        refreshToken = tokenEditor.getRefreshToken();
    }
}
