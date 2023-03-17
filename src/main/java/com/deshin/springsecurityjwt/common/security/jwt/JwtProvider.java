package com.deshin.springsecurityjwt.common.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

  private String secretKey;

  /**
   * 객체 초기화, secretKey를 Base64로 인코딩
   */
  @PostConstruct
  protected void init() {
    this.secretKey = Base64.getEncoder().encodeToString(JwtProperties.SECRET.getBytes());
  }


  /**
   * Jwt 토큰 생성
   *
   * @param username
   * @return
   */
  public String generateJwtToken(String username) {

    Claims claims = Jwts.claims().setSubject(username);

    Date now = new Date();
    Date validity = new Date(now.getTime() + JwtProperties.EXPIRATION_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }


  /**
   * 토큰 검증
   *
   * @param jwtToken
   * @return
   */
  public boolean validateToken(String jwtToken){
    try {
      getAllClaimsFromToken(jwtToken);
    } catch (IllegalArgumentException e) {
      log.error("an error occured during getting username from token");
      e.printStackTrace();
      return false;
    } catch(SignatureException e){
      log.error("Authentication Failed. Username or Password not valid.");
      e.printStackTrace();
      return false;
    }
    return true;
  }


  /**
   * 토큰으로 username 반환
   *
   * @param token
   * @return
   */
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  /**
   * 만료시간 검증
   *
   * @param token
   * @return
   */
  public boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    try {
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }


}
