package com.ohgiraffers.springsecurity.auth.service;

import com.ohgiraffers.springsecurity.command.entity.User;
import com.ohgiraffers.springsecurity.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;


  /* UserDetails : Spring Security가 관리하는 사용자 정보 객체
  * */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    // 1) DB에서 username이 일치하는 회원 조회
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다"));

    // 2) UserDetails 인터페이스를 구현한 객체를 만들어서 반환
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
    );
  }
}
