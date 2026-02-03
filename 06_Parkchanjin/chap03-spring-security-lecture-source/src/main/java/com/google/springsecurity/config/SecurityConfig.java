package com.google.springsecurity.config;

import com.google.springsecurity.jwt.JwtAuthenticationFilter;
import com.google.springsecurity.jwt.JwtTokenProvider;
import com.google.springsecurity.jwt.RestAccessDeniedHandler;
import com.google.springsecurity.jwt.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 웹 시큐리티를 사용하겠다
@EnableMethodSecurity // @PreAuthorize, @PostAuthorize
@RequiredArgsConstructor // 상수를 생성자로 만들어줌(생성자 방식으로 의존성 주입)
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;
  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
  private final RestAccessDeniedHandler restAccessDeniedHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /* Spring Security와 연결된 설정 객체 */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

    // CSRF 처리 비활성화(기본 값 : 활성화)
    // JWT는 세션 이용 X(Stateless) -> CSRF 보호가 필수적이지 않음
    http.csrf(AbstractHttpConfigurer::disable)

        // 세션 로그인을 하지 않겠다 라는 뜻 -> 토큰 로그인 설정하겠다!
        // 세션을 생성하지 않고, SecurityContextHolder 에서 세션 저장 X
        // -> 모든 요청에 독립적, 인증 정보는 클라이언트 요청 시 전달된 토근에 의지한다.
        .sessionManagement(session
            -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        /* 인증, 인가 실패 핸들러 추가 */
        .exceptionHandling( exception ->
            exception
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
        )

        // 요청 http method, url 기준으로 인증, 인가 필요 여부를 설정
        .authorizeHttpRequests(auth
            -> auth
            // 회원 가입, 로그인은 누구나 허용 (인증 필요없음)
            .requestMatchers(
                HttpMethod.POST,
                "/api/v1/users",
                "/api/v1/auth/login",
                "/api/v1/auth/refresh",
                "/api/v1/admin").permitAll()

            // 내 정보 조회는 USER 권한이 필요하다
            .requestMatchers(
                HttpMethod.GET, "api/v1/users/me").hasAuthority("USER")

            // 위에 요청을 제외한 나머지 요청은 인증이 필요함
            .anyRequest().authenticated()
        )
        // UsernamePasswordAuthenticationFilter 앞에
        // JWT 인증 커스텀 필터를 추가
        .addFilterBefore(
            jwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class
        );

    return http.build();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
  }

}
