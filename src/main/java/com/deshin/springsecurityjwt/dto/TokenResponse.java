package com.deshin.springsecurityjwt.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class TokenResponse {
  private String jwtToken;

  public TokenResponse(@NotEmpty String jwtToken) {
    this.jwtToken = jwtToken;
  }
}
