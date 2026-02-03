package com.ohgiraffers.springsecurity.query.mapper;

import com.ohgiraffers.springsecurity.query.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

  UserDTO findUserByUsername(String username);

}
