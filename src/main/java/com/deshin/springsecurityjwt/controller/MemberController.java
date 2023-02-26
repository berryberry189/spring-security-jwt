package com.deshin.springsecurityjwt.controller;

import com.deshin.springsecurityjwt.dto.LoginMemberRequest;
import com.deshin.springsecurityjwt.dto.MemberResponse;
import com.deshin.springsecurityjwt.dto.SignupMemberRequest;
import com.deshin.springsecurityjwt.dto.TokenResponse;
import com.deshin.springsecurityjwt.service.MemberService;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

  private final MemberService memberService;

  @ApiOperation(value = "회원가입 API")
  @PostMapping("/signup")
  public ResponseEntity<MemberResponse> signup(@Valid @RequestBody SignupMemberRequest request) {
    return new ResponseEntity<>(memberService.signup(request), HttpStatus.CREATED);
  }

  @ApiOperation(value = "로그인 API")
  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginMemberRequest request) {
    return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
  }

  @ApiOperation(value = "로그인한 회원 정보 API")
  @GetMapping("/user/me")
  public ResponseEntity<MemberResponse> userMyInfo() {
    return new ResponseEntity<>(memberService.getMyInfo(), HttpStatus.OK);
  }

  @ApiOperation(value = "로그인한 어드민 회원 정보 API")
  @GetMapping("/admin/me")
  public ResponseEntity<MemberResponse> adminMyInfo() {
    return new ResponseEntity<>(memberService.getMyInfo(), HttpStatus.OK);
  }


}
