package com.google.springsecurity.auth.controller;

import com.google.springsecurity.auth.dto.LoginRequest;
import com.google.springsecurity.auth.dto.TokenResponse;
import com.google.springsecurity.auth.service.AuthService;
import com.google.springsecurity.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  /* 로그인 */
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<TokenResponse>> login(
      @RequestBody LoginRequest loginRequest
  ){
    TokenResponse response = authService.login(loginRequest);

    return buildTokenResponse(response);
  }


  /* refresh token을 요청 시 전달 받아
  *  인증된 토큰이면 새 access/refresh token을 발급해서 반환
  * */
  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
      @CookieValue(name = "refreshToken", required = false) String refreshToken
  ) {

    // 쿠키에 refresh token이 없을 경우
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /* refresh token이 문제가 없다면
    *  새 access/refresh token 발급 */
    TokenResponse tokenResponse = authService.refreshToken(refreshToken);

    return buildTokenResponse(tokenResponse);
  }


  /* 로그아웃 */
  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(
      @CookieValue(name = "refreshToken", required = false) String refreshToken
  ) {
    // refreshToken이 존재할 경우 == 로그인 상태
    if (refreshToken != null){
      authService.logout(refreshToken); // DB refreshToken 삭제
    }

    ResponseCookie deleteCookie
        = createDeleteRefreshTokenCookie();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
        .body(ApiResponse.success(null));
  }




  /* accessToken 과 refreshToken을 body와 쿠키에 담아 반환 */
  private ResponseEntity<ApiResponse<TokenResponse>> buildTokenResponse(TokenResponse tokenResponse) {
    ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());  // refreshToken 쿠키 생성
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(ApiResponse.success(tokenResponse));
  }


  /* refreshToken 쿠키 생성 */
  private ResponseCookie createRefreshTokenCookie(String refreshToken) {
    return ResponseCookie.from("refreshToken", refreshToken)
        .httpOnly(true)                     // HttpOnly 속성 설정 (JavaScript 에서 접근 불가)
        // .secure(true)                    // HTTPS 환경일 때만 전송 (운영 환경에서 활성화 권장)
        .path("/")                          // 쿠키 범위 : 전체 경로
        .maxAge(Duration.ofDays(7))         // 쿠키 만료 기간 : 7일
        .sameSite("Strict")                 // CSRF 공격 방어를 위한 SameSite 설정
        .build();
  }


  /* refreshToken 쿠키 삭제용 설정 */
  private ResponseCookie createDeleteRefreshTokenCookie() {
    return ResponseCookie.from("refreshToken", "")
        .httpOnly(true)                     // HttpOnly 속성 설정 (JavaScript 에서 접근 불가)
        // .secure(true)                    // HTTPS 환경일 때만 전송 (운영 환경에서 활성화 권장)
        .path("/")                          // 쿠키 범위 : 전체 경로
        .maxAge(0)         // 쿠키 만료 기간 : 0초 -> 즉시 만료
        .sameSite("Strict")                 // CSRF 공격 방어를 위한 SameSite 설정
        .build();
  }

}
