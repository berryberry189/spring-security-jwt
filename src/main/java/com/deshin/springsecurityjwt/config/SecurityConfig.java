package com.deshin.springsecurityjwt.config;

import com.deshin.springsecurityjwt.common.security.jwt.JwtAccessDeniedHandler;
import com.deshin.springsecurityjwt.common.security.jwt.JwtAuthenticationEntryPoint;
import com.deshin.springsecurityjwt.common.security.jwt.JwtAuthenticationFilter;
import com.deshin.springsecurityjwt.common.security.jwt.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CorsFilter corsFilter;

  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtExceptionFilter jwtExceptionFilter;


  private static final String[] PERMIT_URL_ARRAY = {
      /* swagger v2 */
      "/v2/api-docs",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui.html",
      "/webjars/**",
      "/swagger/**",

      /* swagger v3 */
      "/v3/api-docs/**",
      "/swagger-ui/**",

      /* h2 */
      "/h2-console/**",

      /* 로그인, 회원가입 api */
      "/**/signup",
      "/**/login"
  };

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().antMatchers(PERMIT_URL_ARRAY);
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable(); // rest api 이므로 서버에 인증정보를 저장하지 않고 SessionCreationPolicy.STATELESS 설정 할 것이므로 disable 처리

    http.authorizeRequests()
        .mvcMatchers(PERMIT_URL_ARRAY).permitAll()
        .anyRequest().authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(corsFilter)
        .formLogin().disable()
        .httpBasic().disable(); // 로그인 폼 화면 사용 X

    // jwt 예외 핸들러
    http.exceptionHandling()
        .accessDeniedHandler(jwtAccessDeniedHandler)
        .authenticationEntryPoint(jwtAuthenticationEntryPoint);

    // 커스텀 필터설정
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

    return http.build();
  }


}
