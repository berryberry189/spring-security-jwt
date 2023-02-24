package com.deshin.springsecurityjwt.service;

import static com.deshin.springsecurityjwt.common.security.util.AuthenticationUtils.getUserId;

import com.deshin.springsecurityjwt.common.security.jwt.JwtProvider;
import com.deshin.springsecurityjwt.common.security.provider.CustomAuthenticationProvider;
import com.deshin.springsecurityjwt.domain.Member;
import com.deshin.springsecurityjwt.dto.LoginMemberRequest;
import com.deshin.springsecurityjwt.dto.MemberResponse;
import com.deshin.springsecurityjwt.dto.SignupMemberRequest;
import com.deshin.springsecurityjwt.dto.TokenResponse;
import com.deshin.springsecurityjwt.repository.MemberRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtProvider jwtProvider;
  private final CustomAuthenticationProvider customAuthenticationProvider;

  /**
   * 회원가입
   */
  @Transactional
  public MemberResponse signup(SignupMemberRequest request) {

    memberRepository.findByUserId(request.getUserId())
        .ifPresent(i -> {
          throw new IllegalArgumentException("이미 가입된 id 입니다.");
        });

    Member savedMember = memberRepository.save(
        request.toEntity(bCryptPasswordEncoder.encode(request.getPassword())));

    return new MemberResponse(savedMember);
  }


  /**
   * 로그인
   */
  @Transactional
  public TokenResponse login(LoginMemberRequest request) {
    // 유저인증
    customAuthenticationProvider.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword()));

    String jwtToken = jwtProvider.generateJwtToken(request.getUserId());
    return new TokenResponse(jwtToken);
  }

  /**
   * 내정보
   */
  @Transactional(readOnly = true)
  public MemberResponse getMyInfo() {
    Member member = getMemberEntity(getUserId());
    return new MemberResponse(member);
  }

  /**
   * Member Entity 반환
   */
  private Member getMemberEntity(String userId) {
    return memberRepository.findByUserId(userId)
        .orElseThrow(() -> new EntityNotFoundException("가입되지 않은 아이디입니다. userId = " + userId));
  }
}
