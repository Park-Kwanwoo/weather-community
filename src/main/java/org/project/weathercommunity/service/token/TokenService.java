package org.project.weathercommunity.service.token;

import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.token.Token;
import org.project.weathercommunity.domain.token.TokenEditor;
import org.project.weathercommunity.exception.TokenNotFoundException;
import org.project.weathercommunity.repository.token.TokenRepository;
import org.project.weathercommunity.request.token.TokenRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public void saveToken(TokenRequest tokenRequest, Member member) {

        // 비정상적으로 종료되어 토큰이 삭제되지 않았을 때
        // 토큰이 존재한다면 제거
        tokenRepository.findByMember(member).ifPresent(token -> {
            tokenRepository.deleteById(token.getId());
        });

        Token token = Token.builder()
                .accessToken(tokenRequest.getAccessToken())
                .refreshToken(tokenRequest.getRefreshToken())
                .member(member)
                .build();

        tokenRepository.save(token);
    }

    @Transactional
    public String getRefreshToken(Member member) {

        Token token = tokenRepository.findByMember(member)
                .orElseThrow(TokenNotFoundException::new);

        return token.getRefreshToken();
    }

    @Transactional
    public void reIssueToken(TokenEditor tokenEdit, Member member) {

        Token token = tokenRepository.findByMember(member)
                .orElseThrow(TokenNotFoundException::new);

        TokenEditor.TokenEditorBuilder editorBuilder = token.toEditor();

        TokenEditor tokenEditor = editorBuilder
                .accessToken(tokenEdit.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();

        token.edit(tokenEditor);
    }

    @Transactional
    public void deleteToken(Member member) {

        Token token = tokenRepository.findByMember(member)
                .orElseThrow(TokenNotFoundException::new);

        tokenRepository.delete(token);
    }
}
