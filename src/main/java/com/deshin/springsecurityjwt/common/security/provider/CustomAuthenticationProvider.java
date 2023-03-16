package com.deshin.springsecurityjwt.common.security.provider;

import com.deshin.springsecurityjwt.common.security.dto.UserDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    UserDetailsResponse userDetailsResponse = (UserDetailsResponse) userDetailsService.loadUserByUsername(username);

    if(ObjectUtils.isEmpty(userDetailsResponse)) {
      throw new UsernameNotFoundException("아이디를 찾을 수 없습니다. userId = " + username);
    }

    if(!bCryptPasswordEncoder.matches(password, userDetailsResponse.getPassword())) {
      throw new BadCredentialsException("비밀번호가 맞지 않습니다.");
    }

    return new UsernamePasswordAuthenticationToken(userDetailsResponse.getUsername(), userDetailsResponse.getPassword(), userDetailsResponse.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    // 매개변수 authentication 이 UsernamePasswordAuthenticationToken.class 와 동일한지 체크
    return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
  }
}
