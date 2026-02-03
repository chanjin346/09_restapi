package com.google.springsecurity.command.controller;

import com.google.springsecurity.command.dto.UserCreateRequest;
import com.google.springsecurity.command.service.UserCommandService;
import com.google.springsecurity.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1") // 버전정보 기록용으로 사용
public class UserCommandController {
  private final UserCommandService userCommandService;

  /* 회원 가입 - USER 권한 */
  @PostMapping("/users")
  public ResponseEntity<ApiResponse<Void>> register(
    @RequestBody UserCreateRequest userCreateRequest
    ){

    // 서비스 호출
    userCommandService.registUser(userCreateRequest);

    return ResponseEntity
        .status(HttpStatus.CREATED) // 201 Create(삽입/저장 성공)
        .body(ApiResponse.success(null));

  }


  /* 회원 가입 - ADMIN 권한 */
  @PostMapping("/admin")
  public ResponseEntity<ApiResponse<Void>> registerAdmin(
      @RequestBody UserCreateRequest userCreateRequest
  ){

    // 서비스 호출
    userCommandService.registAdmin(userCreateRequest);

    return ResponseEntity
        .status(HttpStatus.CREATED) // 201 Create(삽입/저장 성공)
        .body(ApiResponse.success(null));

  }
}
