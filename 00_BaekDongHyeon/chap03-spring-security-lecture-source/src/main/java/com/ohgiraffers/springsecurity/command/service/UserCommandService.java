package com.ohgiraffers.springsecurity.command.service;

import com.ohgiraffers.springsecurity.command.dto.UserCreateRequest;
import com.ohgiraffers.springsecurity.command.entity.User;
import com.ohgiraffers.springsecurity.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void registUser(UserCreateRequest userCreateRequest){

    /* request(DTO) -> Entity */
    User user = modelMapper.map(userCreateRequest, User.class);

    /* 비밀번호를 암호화 하여 Entity에 세팅 */
    user.setEncodedPassword(
        passwordEncoder.encode(userCreateRequest.getPassword())
    );

    /* 저장 */
    userRepository.save(user);
  }


}
