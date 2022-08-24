package org.project.weathercommunity.repository.member;

import org.project.weathercommunity.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


}
