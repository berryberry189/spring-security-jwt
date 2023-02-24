package com.deshin.springsecurityjwt.dto;

import com.deshin.springsecurityjwt.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(description = "회원 Response DTO")
public class MemberResponse {

  @ApiModelProperty(value = "member id")
  private Long id;

  @ApiModelProperty(value = "아이디")
  private String userId;

  @ApiModelProperty(value = "이름")
  private String name;

  @ApiModelProperty(value = "가입일")
  private LocalDateTime createAt;

  public MemberResponse (Member member) {
    this.id = member.getId();
    this.userId = member.getUserId();
    this.name = member.getName();
    this.createAt = member.getCreateAt();
  }

}
