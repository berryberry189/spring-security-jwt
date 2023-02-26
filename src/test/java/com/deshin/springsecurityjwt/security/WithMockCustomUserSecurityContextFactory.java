package com.deshin.springsecurityjwt.security;

import com.deshin.springsecurityjwt.common.security.dto.UserDetailsResponse;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    UserDetailsResponse principal =
        new UserDetailsResponse(customUser.userId(), "password", List.of("ROLE_USER"));
    Authentication auth =
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }

}
