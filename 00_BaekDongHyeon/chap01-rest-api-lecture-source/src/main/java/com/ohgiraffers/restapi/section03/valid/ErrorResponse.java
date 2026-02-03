package com.ohgiraffers.restapi.section03.valid;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponse {
   /* 예외 발생 시 에러 메시지를 전달하는 용도의 객체 */

  private String code;
  private String description;
  private String detail;

}