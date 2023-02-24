package com.deshin.springsecurityjwt.common.security.jwt;

import com.deshin.springsecurityjwt.common.exception.ErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 인증 과정에서 실패하거나 인증을 위한 헤더정보를 보내지 않은 경우 동작
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
    log.error("UnAuthorized -- message : " + e.getMessage());
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    response.setStatus(status.value());
    response.setContentType("application/json; charset=UTF-8");
    ErrorResponse errorResponse = new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage());
    response.getWriter().write(errorResponse.convertToJson());
  }
}