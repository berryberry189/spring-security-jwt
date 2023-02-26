package com.deshin.springsecurityjwt.common.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsResponse implements UserDetails {

  private String userId;
  private String password;
  private List<String> roleList;

  public UserDetailsResponse(String userId, String password, List<String> roleList) {
    this.userId = userId;
    this.password = password;
    this.roleList = roleList;
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
    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    roleList.forEach(role -> authorities.add(()->{ return role;}));
    return authorities;
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
