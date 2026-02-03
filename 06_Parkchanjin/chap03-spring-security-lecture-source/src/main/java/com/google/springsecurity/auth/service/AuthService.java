package com.google.springsecurity.auth.service;

import com.google.springsecurity.auth.dto.LoginRequest;
import com.google.springsecurity.auth.dto.TokenResponse;
import com.google.springsecurity.auth.entity.RefreshToken;
import com.google.springsecurity.auth.repository.AuthRepository;
import com.google.springsecurity.command.entity.User;
import com.google.springsecurity.command.repository.UserRepository;
import com.google.springsecurity.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthRepository authRepository;

  public TokenResponse login(LoginRequest request){

    // 1. ID(username) 로 조회 -> id(username), pwd(암호화) 조회됨
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다."));

    // 2. 비밀번호 매칭 확인
    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
      throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    // 3. 비밀번호 일치 -> 로그인 성공 -> 토큰 발급
    String accessToken
        = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
    String refreshToken
        = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRole().name());


    // 4. RefreshToken 저장(보안 및 토큰 재발급 검증용)
    RefreshToken tokenEntity = RefreshToken.builder()
        .username(user.getUsername())
        .token(refreshToken)
        .expiryDate(
            new Date(System.currentTimeMillis()
                + jwtTokenProvider.getRefreshExpiration())
        ).build();


    authRepository.save(tokenEntity);



    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

  }

  /* refresh token 검증 후 새 토큰 발급 서비스 */
  public TokenResponse refreshToken(String provideRefreshToken) {

    // 리프레시 토큰 유효성 검사
    jwtTokenProvider.validateToken(provideRefreshToken);

    // 전달 받은 리프레시 토큰에서 사용자 이름(username) 얻어오기
    String username = jwtTokenProvider.getUsernameFromJWT(provideRefreshToken);

    // DB에서 username이 일치하는 행의 리프레시 토큰 조회
    RefreshToken storedToken = authRepository.findById(username)
        .orElseThrow(()
            -> new BadCredentialsException("해당 유저로 조회되는 리프레시 토큰 없음"));

    // 요청 시 전달 받은 토큰과
    // DB에 저장된 토큰이 일치하는지 확인
    if(!storedToken.getToken().equals(provideRefreshToken)){
      throw new BadCredentialsException("리프레시 토큰이 일치하지 않음");
    }

    // 요청 시 전달받은 토큰의 만료기간이 현재 시간보다 과거인지 확인
    // (만료 기간이 지났는지 확인)
    if (storedToken.getExpiryDate().before(new Date())){
      throw new BadCredentialsException("리프레시 토큰 유효기간 만료");
    }

    // username이 일치하는 회원(User) 조회
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new BadCredentialsException("해당 유저 없음"));

    // 새 토큰 발급
    String accessToken
        = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());

    String refreshToken
        = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRole().name());

    // RefreshToken 엔티티 생성(저장용)
    RefreshToken tokenEntity = RefreshToken.builder()
        .username(username)
        .token(refreshToken)
        .expiryDate(
            new Date(
                System.currentTimeMillis()
                + jwtTokenProvider.getRefreshExpiration()
            )
        )
        .build();

    // DB 저장(PK 중복 행이 이미 존재 -> UPDATE)
    authRepository.save(tokenEntity);

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }


  /* DB refreshToken */
  public void logout(String refreshToken) {

    /* refreshToken 검증 절차 */
    jwtTokenProvider.validateToken(refreshToken);

    String username = jwtTokenProvider.getUsernameFromJWT(refreshToken);

    authRepository.deleteById(username); // DB에서 username이 일치하는 행 삭제

  }
}

