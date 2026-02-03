package com.ohgiraffers.restapi.section03.valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController // @Controller + @ResponseBody
@RequestMapping("/valid")
public class ValidTestController {

  @GetMapping("/users/{userNo}")
  public ResponseEntity<?> findUserByNo() throws UserNotFoundException {

    boolean check = true;
    if(check) {
      throw new UserNotFoundException("회원 정보를 찾을 수 없습니다.");
    }

    return ResponseEntity.ok().build();
  }


  /* 유저 등록 핸들러 메서드
  * - RequestBody를 통해 전달 받은 데이터 UserDTO 필드 값
  *   유효성 검사 수행(@Validated)
  * */
  @PostMapping("/users")
  public ResponseEntity<?> registUser(@Validated @RequestBody UserDTO user) {

    System.out.println(user);

    return ResponseEntity
        .created(URI.create("/valid/users/" + "userNo"))
        .build();

  }
}
