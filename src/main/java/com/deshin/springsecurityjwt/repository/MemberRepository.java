package com.deshin.springsecurityjwt.repository;

import com.deshin.springsecurityjwt.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByUserId(String userId);

}
