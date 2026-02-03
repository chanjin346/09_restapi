package com.ohgiraffers.springsecurity.query.service;

import com.ohgiraffers.springsecurity.query.dto.UserDTO;
import com.ohgiraffers.springsecurity.query.dto.UserDetailResponse;
import com.ohgiraffers.springsecurity.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryService {

  private final UserMapper userMapper;

  public UserDetailResponse getUserDetail(String username) {

    UserDTO user = Optional.ofNullable(
        userMapper.findUserByUsername(username)
    ).orElseThrow(() -> new RuntimeException("사용자 찾지 못함"));

    return UserDetailResponse.builder()
        .user(user)
        .build();
  }
}
