package com.deshin.springsecurityjwt.common.exception;

import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 유효하지 않은 입력 값 오류 (400)
   */
  @ExceptionHandler(value = {IllegalArgumentException.class, TypeMismatchException.class})
  public ResponseEntity<ErrorResponse> invalidArgumentException(RuntimeException e) {
    log.error("invalidArgumentException", e);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage()), status);
  }

  /**
   * Validation을 통한 Request 값 검증 오류 (400)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("methodArgumentNotValidException", e);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    //유효성 검증 실패한 필드, 에러 메시지 정보
    String errorField = e.getBindingResult().getFieldError().getField();
    String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
    String errorMessage = errorField + ": " + defaultMessage;
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), errorMessage), status);
  }

  /**
   * 로그인 시 비밀번호 맞지 않는 오류 (403)
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> badCredentialsException(BadCredentialsException e) {
    log.error("badCredentialsException", e);
    HttpStatus status = HttpStatus.FORBIDDEN;
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage()), status);
  }

  /**
   * 로그인 시 유저 찾을 수 없는 오류 (401)
   */
  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> usernameNotFoundException(UsernameNotFoundException e) {
    log.error("usernameNotFoundException", e);
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage()), status);
  }


  /**
   * 엔티티 조회 실패 오류 (404)
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException e) {
    log.error("entityNotFoundException", e);
    HttpStatus status = HttpStatus.NOT_FOUND;
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage()), status);
  }


  /**
   * 서버 장애에 대한 커스텀 처리
   */
  @ExceptionHandler(value = {RuntimeException.class})
  public ResponseEntity<ErrorResponse> customException(RuntimeException e, HttpStatus status) {
    log.error("customException", e);
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage()), status);
  }

}
