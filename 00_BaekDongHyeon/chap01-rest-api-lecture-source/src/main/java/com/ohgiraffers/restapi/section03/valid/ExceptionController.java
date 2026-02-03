package com.ohgiraffers.restapi.section03.valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/* 프로젝트 전역에서 발생하는 Exception을 처리하기 위한 클래스 */
@RestControllerAdvice // (@RestController 예외만 처리)
public class ExceptionController {

  /**
   * 프로젝트 전역에서 발생하는 UserNotFoundException 처리 메서드
   *
   * @param e
   * @return
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse>
  handleUserException(UserNotFoundException e) {

    String code = "ERROR_CODE_00000";
    String description = "회원 정보 조회 실패";
    String detail = e.getMessage();

//    return new ResponseEntity<>(
//        new ErrorResponse(code, description, detail),
//        HttpStatus.NOT_FOUND
//    );

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(code, description, detail));
  }


  /* MethodArgumentNotValidException : @Validation 어노테이션 발생 예외 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> methodValidException(
      MethodArgumentNotValidException e
  ) {

    String code = "";
    String description = "";
    String detail = "";

    /* 에러가 있다면 */
    if (e.getBindingResult().hasErrors()) {
      detail
          = e.getBindingResult().getFieldError().getDefaultMessage();

      String bindResultCode
          = e.getBindingResult().getFieldError().getCode();
      System.out.println(bindResultCode);

      switch (bindResultCode) {
        case "NotNull":
          code = "ERROR_CODE_00001";
          description = "필수 값이 누락되었습니다.";
          break;
        case "NotBlank":
          code = "ERROR_CODE_00002";
          description = "필수 값이 공백으로 처리되었습니다.";
          break;
        case "Size":
          code = "ERROR_CODE_00003";
          description = "알맞은 크기의 값이 입력되지 않았습니다.";
          break;
        case "Length":
          code = "ERROR_CODE_00004";
          description = "알맞은 길이의 값이 입력되지 않았습니다.";
          break;
      }
    }

    ErrorResponse errorResponse
        = new ErrorResponse(code, description, detail);

//    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    return ResponseEntity.badRequest().body(errorResponse);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }


}
