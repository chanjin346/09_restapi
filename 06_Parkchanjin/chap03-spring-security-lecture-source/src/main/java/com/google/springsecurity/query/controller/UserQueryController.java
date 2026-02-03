package com.google.springsecurity.query.controller;

import com.google.springsecurity.common.ApiResponse;
import com.google.springsecurity.query.dto.UserDetailResponse;
import com.google.springsecurity.query.dto.UserListResponse;
import com.google.springsecurity.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1") // 버전정보 기록용으로 사용
public class UserQueryController {

  private final UserQueryService userQueryService;

  // @AuthenticationPrincipal UserDetails userDetails
  // -> 인증 필터를 거쳐 Spring Security Context에 저장된
  // 인증 객체를 얻어오는 어노테이션
  // + UserDetails <- User <- CustomUser 형태로 상속하여
  //   id, pwd, authorities 외에 추가 정보도 담을 수 있다


  /* 로그인한 회원 정보 조회 */
  @GetMapping("/users/me")
  public ResponseEntity<ApiResponse<UserDetailResponse>> getUserDetail(
      @AuthenticationPrincipal UserDetails userDetails
      ){
    UserDetailResponse response
        = userQueryService.getUserDetail(userDetails.getUsername());

    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /* 관리자 권한을 가진 회원만 가능한 요청
  *  회원 전체 목록 조회 */
  @PreAuthorize("hasAuthority('ADMIN')") //메서드 수행 전 admin 권한을 먼저 인증해라 라는 뜻
  @GetMapping("/admin/users")
  public ResponseEntity<ApiResponse<UserListResponse>> getUsers() {
    UserListResponse response = userQueryService.getAllUser();
    return ResponseEntity.ok(ApiResponse.success(response));
  }

}
