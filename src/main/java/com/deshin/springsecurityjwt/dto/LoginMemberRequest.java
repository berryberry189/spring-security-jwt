package com.deshin.springsecurityjwt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(description = "로그인 Request DTO")
public class LoginMemberRequest {

  @NotBlank(message = "아이디를 입력해주세요")
  @ApiModelProperty(value = "아이디", required = true, example = "userid01")
  private String userId;

  @NotBlank(message = "비밀번호를 입력해주세요")
  @ApiModelProperty(value = "비밀번호", required = true, example = "123456")
  private String password;

  public LoginMemberRequest(@NotBlank String userId, @NotBlank String password) {
    this.userId = userId;
    this.password = password;
  }

}
