package com.deshin.springsecurityjwt.dto;

import com.deshin.springsecurityjwt.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(description = "회원 가입 Request DTO")
public class SignupMemberRequest {

  @NotBlank(message = "아이디를 입력해주세요")
  @Size(min = 4, max = 12, message = "아이디는 4글자 이상, 12글자 이하로 입력해주세요.")
  @ApiModelProperty(value = "아이디", required = true, example = "userid01")
  private String userId;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  @Size(min = 6, max = 20, message = "비밀번호는 6글자 이상, 20글자 이하로 입력해주세요.")
  @ApiModelProperty(value = "비밀번호", required = true, example = "123456")
  private String password;

  @NotBlank(message = "이름을 입력해주세요")
  @Size(min = 2, max = 10, message = "이름은 2글자 이상, 10글자 이하로 입력해주세요.")
  @ApiModelProperty(value = "이름", required = true, example = "김유저")
  private String name;

  public SignupMemberRequest(@NotBlank String userId, @NotBlank String password, @NotBlank String name) {
    this.userId = userId;
    this.password = password;
    this.name = name;
  }

  public Member toEntity(String encryptedPassword) {
    return Member.builder()
        .userId(this.userId)
        .password(encryptedPassword)
        .name(this.name)
        .build();
  }


}
