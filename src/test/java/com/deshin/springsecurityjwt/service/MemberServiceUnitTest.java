package com.deshin.springsecurityjwt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.deshin.springsecurityjwt.domain.Member;
import com.deshin.springsecurityjwt.dto.MemberResponse;
import com.deshin.springsecurityjwt.dto.SignupMemberRequest;
import com.deshin.springsecurityjwt.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceUnitTest {

  @InjectMocks
  private MemberService memberService;

  @Mock
  private MemberRepository memberRepository;

  @Spy
  private BCryptPasswordEncoder bCryptPasswordEncoder;


  @DisplayName("회원가입 유닛 테스트")
  @Test
  void signup() {
    // given
    bCryptPasswordEncoder = new BCryptPasswordEncoder();
    SignupMemberRequest request = getSignupMemberRequest();
    String encryptedPassword = bCryptPasswordEncoder.encode(request.getPassword());

    doReturn(new Member(request.getUserId(), encryptedPassword, request.getName(), "ROLE_USER"))
        .when(memberRepository)
        .save(any(Member.class));

    // when
    MemberResponse member = memberService.signup(request);

    // then
    assertThat(member.getUserId()).isEqualTo(request.getUserId());

    // verify
    verify(memberRepository, times(1)).save(any(Member.class));
  }



  private SignupMemberRequest getSignupMemberRequest() {
    return new SignupMemberRequest("userid01", "123456", "김유저");
  }


}