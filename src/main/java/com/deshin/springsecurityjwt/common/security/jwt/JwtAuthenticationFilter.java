package com.deshin.springsecurityjwt.common.security.jwt;

import io.jsonwebtoken.JwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 인증은 성공되었으나 expire time 이나, 권한이 있을경우 권한을 체크하는 filter
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
    String header = req.getHeader(JwtProperties.HEADER_STRING);
    String accessToken =
        (header != null && header.startsWith(JwtProperties.TOKEN_PREFIX)) ?
            header.replace(JwtProperties.TOKEN_PREFIX, "") : null;

    if (!ObjectUtils.isEmpty(accessToken)) {
      // Access Token 이 만료된 경우
      if (jwtProvider.isTokenExpired(accessToken)) {
        throw new JwtException("token이 만료되었습니다.");
      }

      // Access Token 이 유효한 경우
      if (jwtProvider.validateToken(accessToken)) {
        String username = jwtProvider.getUsernameFromToken(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        logger.info("authenticated user " + username + ", setting security context");

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } else {
      logger.warn("token 이 존재하지 않음");
    }
    chain.doFilter(req, res);
  }

}
