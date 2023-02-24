package com.deshin.springsecurityjwt.common.security.jwt;

import com.deshin.springsecurityjwt.common.exception.ErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 유저 정보는 있으나, 엑세스 권한이 없는 경우 동작
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
    log.error("Forbidden -- message : " + e.getMessage());
    HttpStatus status = HttpStatus.FORBIDDEN;
    response.setStatus(status.value());
    response.setContentType("application/json; charset=UTF-8");
    ErrorResponse errorResponse = new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage());
    response.getWriter().write(errorResponse.convertToJson());
  }
}
