package com.mycompany.restapi.section02.responseentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseMessage {

  /* 응답 메세지용 클래스 */
    private int httpStatus; // 응답 상태 코드(200,201,400,500...)
    private String message; // 응답으로 전달할 메세지
    private Map<String, Object> results; // 응답 시 전달할 데이터

}