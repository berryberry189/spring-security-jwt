package com.deshin.springsecurityjwt.common.security.util;

import com.deshin.springsecurityjwt.common.security.dto.UserDetailsResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {

  public static UserDetailsResponse getUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("authentication.getName() : " + authentication.getName());

    return (UserDetailsResponse) authentication.getPrincipal();
  }

  public static String getUserId() {
    UserDetailsResponse userDetails = getUserDetails();
    return userDetails != null ? userDetails.getUsername() : null;
  }


}
