package com.deshin.springsecurityjwt.common.security.service;

import com.deshin.springsecurityjwt.domain.Member;
import com.deshin.springsecurityjwt.repository.MemberRepository;
import com.deshin.springsecurityjwt.common.security.dto.UserDetailsResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    Optional<Member> memberOptional = memberRepository.findByUserId(userId);
    if(memberOptional.isPresent()) {
      Member member = memberOptional.get();
      return new UserDetailsResponse(member.getUserId(), member.getPassword());
    }
    return null;
  }
}
