package com.google.springsecurity.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component // bean 등록
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpiration;

  @Value("${jwt.refresh-expiration}")
  private long jwtRefreshExpiration;

  private SecretKey secretKey;

  @PostConstruct // 생성된 후에 실행됨
  public void init() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  /* access token 생성 메서드 */
  public String createToken(String username, String role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpiration); // 1970 년 1월 1일이 0초임 !

    // JWT 토근 생성 -> Header, Payload(Claims), Siignnature

    return Jwts.builder()
        .subject(username)  // Payload: subject (보통 사용자를 식별할 수 있는값을 넣음)(등록 claim)
        .claim("role", role)  // Payload: subject (권한 정보) (공개 Claim)
        .issuedAt(now)      // Payload: IssuedAt (발행 시간)
        .expiration(expiryDate)  // Payload: Expiration Time(만료 시간)
        .signWith(secretKey) // signature : 비밅키 서명(위변조 방지)
        .compact();
  }


  /* access token 생성 메서드 */
  public String createRefreshToken(String username, String role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration); // 1970 년 1월 1일이 0초임 !

    // JWT 토근 생성 -> Header, Payload(Claims), Siignnature

    return Jwts.builder()
        .subject(username)  // Payload: subject (보통 사용자를 식별할 수 있는값을 넣음)(등록 claim)
        .claim("role", role)  // Payload: subject (권한 정보) (공개 Claim)
        .issuedAt(now)      // Payload: IssuedAt (발행 시간)
        .expiration(expiryDate)  // Payload: Expiration Time(만료 시간)
        .signWith(secretKey) // signature : 비밀키 서명(위변조 방지)
        .compact();
  }

  // refresh token 만료 시간 반환
  public long getRefreshExpiration(){
    return jwtRefreshExpiration;
  }

  /* JWT 토큰 유효성 검사 메서드 */
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      throw new BadCredentialsException("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      throw new BadCredentialsException("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      throw new BadCredentialsException("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      throw new BadCredentialsException("JWT Token claims empty", e);
    }

  }

  /* JWT 토큰 중 payload -> claim -> subject의 값 반환 */
  public String getUsernameFromJWT(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return claims.getSubject();
  }
}
