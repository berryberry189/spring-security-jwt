package com.deshin.springsecurityjwt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.deshin.springsecurityjwt.domain.Member;
import com.deshin.springsecurityjwt.dto.LoginMemberRequest;
import com.deshin.springsecurityjwt.dto.MemberResponse;
import com.deshin.springsecurityjwt.dto.SignupMemberRequest;
import com.deshin.springsecurityjwt.dto.TokenResponse;
import com.deshin.springsecurityjwt.repository.MemberRepository;
import com.deshin.springsecurityjwt.security.WithMockCustomUser;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@SpringBootTest
class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  MemberRepository memberRepository;


  private final String userId = "userid01";
  private final String password = "123456";
  private final String name = "김유저";

  @AfterEach
  void deleteMember() {
    memberRepository.deleteAll();
  }

  @DisplayName("회원가입 성공 테스트")
  @Test
  void signupSuccess() {
    SignupMemberRequest request = getSignupMemberRequest(name);
    memberService.signup(request);

    Member member = memberRepository.findByUserId(userId).get();

    Assertions.assertEquals(userId, member.getUserId());
  }


  @DisplayName("회원가입 실패 테스트 - 중복가입요청")
  @Test
  void signupDuplicateFail() {
    SignupMemberRequest request = getSignupMemberRequest(name);
    memberService.signup(request);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> memberService.signup(request));

    assertEquals("이미 가입된 id 입니다.", exception.getMessage());
  }

  @DisplayName("로그인 성공 테스트")
  @Test
  void loginSuccess() {
    SignupMemberRequest request = getSignupMemberRequest(name);
    memberService.signup(request);

    TokenResponse tokenResponse = memberService.login(new LoginMemberRequest(userId, password));

    assertNotNull(tokenResponse);
  }

  @DisplayName("로그인 실패 테스트 - 로그인 아이디 없음")
  @Test
  void loginUserIdFail() {
    SignupMemberRequest request = getSignupMemberRequest(name);
    memberService.signup(request);

    UsernameNotFoundException exception =
        assertThrows(UsernameNotFoundException.class, () -> memberService.login(new LoginMemberRequest("userId02", password)));

    assertEquals("아이디를 찾을 수 없습니다. userId = " + "userId02", exception.getMessage());
  }

  @DisplayName("로그인 실패 테스트 - 패스워드 다름")
  @Test
  void loginDifferentPasswordFail() {
    SignupMemberRequest request = getSignupMemberRequest(name);
    memberService.signup(request);

    BadCredentialsException exception =
        assertThrows(BadCredentialsException.class, () -> memberService.login(new LoginMemberRequest(userId, "1234")));

    assertEquals("비밀번호가 맞지 않습니다.", exception.getMessage());
  }

  @DisplayName("내정보 보기 성공 테스트")
  @Test
  @WithMockCustomUser(userId = userId)
  void myInfoSuccess() {
    SignupMemberRequest request = getSignupMemberRequest(name);
    memberService.signup(request);

    MemberResponse myInfo = memberService.getMyInfo();

    assertEquals(name, myInfo.getName());
  }

  @DisplayName("내정보 보기 실패 테스트 - id 정보 없음")
  @Test
  @WithMockCustomUser(userId = userId)
  void myInfoFail() {
    EntityNotFoundException exception =
        assertThrows(EntityNotFoundException.class, () -> memberService.getMyInfo());

    assertEquals("가입되지 않은 아이디입니다. userId = " + userId, exception.getMessage());
  }



  private SignupMemberRequest getSignupMemberRequest(String name) {
    return new SignupMemberRequest(userId, password, name);
  }


}