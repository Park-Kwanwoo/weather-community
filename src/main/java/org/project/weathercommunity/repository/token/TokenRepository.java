package org.project.weathercommunity.repository.token;

import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByMember(Member member);
}
