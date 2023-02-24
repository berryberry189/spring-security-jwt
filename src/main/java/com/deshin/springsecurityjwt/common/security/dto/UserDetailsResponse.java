package com.deshin.springsecurityjwt.common.security.dto;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsResponse implements UserDetails {

  private String userId;
  private String password;

  public UserDetailsResponse(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }

  @Override
  public String getUsername() {
    return userId;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isEnabled();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public boolean isAccountNonLocked() {
    return isEnabled();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isEnabled();
  }

  @Override
  public boolean isEnabled() {
    // 휴면계정 기간 있는경우 false 로 줄 수 있음
    return true;
  }

}
