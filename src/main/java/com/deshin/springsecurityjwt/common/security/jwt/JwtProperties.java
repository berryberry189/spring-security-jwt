package com.deshin.springsecurityjwt.common.security.jwt;

public interface JwtProperties {
	String SECRET = "jwt_secret";
	int EXPIRATION_TIME =  60 * 60 * 1000; // 1시간
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";
}
