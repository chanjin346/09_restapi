package com.google.restapi.section01.response;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/* HttpMessageConverter
*  - Spring에서 HTTP 요청/응답을 알맞은 형태로 자동 변환해주는 객체
*  - 숫자, 문자열 -> text/plain
*  - Object 타입  -> appication/json
*  - 파일         -> 지정된 produces MIME TYPE
* */
@RestController // @ResponseBody + @Controller
                // 모든 핸들러 메서드가 데이터로만 응답을 하는 컨트롤러
@RequestMapping("/response")
public class ResponseControoler {

  /* 1. 문자열 응답 */
  @GetMapping("/hello")
  //@ResponseBody // View Resolver를 찾아가지 않고
                // 응답 body에 담겨 그대로 클라이언트에게 응답
  public String helloWorld(){
    return "helloWorld!";
  }

  /* 2. Object 응답 */
  @GetMapping("/message")
  public Message getMessage() {
    return new Message(200, "메시지를 응답합니다.");
  }

  /* 3. List 응답 */
  @GetMapping("/list")
  public List<String> getList() {
    return List.of(new String[] {"사과", "딸기", "바나나"});
  }

  /* 4. Map 응답 */
  @GetMapping("/map")
  public Map<Integer, String> getMap() {

    List<Message> messageList = new ArrayList<>();
    messageList.add(new Message(200, "정상 응답"));
    messageList.add(new Message(404, "페이지를 찾을 수 없습니다"));
    messageList.add(new Message(500, "개발자의 잘못입니다"));

    return messageList.stream() // list를 key value 형태의 map으로 변환
        .collect(Collectors.toMap(Message::getHttpStatusCode, Message::getMessage));

  }

  /* 5. file 응답
  * produces : 해당 api의 응답 데이터 타입을 명시하는 속성
  *
  * MediaType.IMAGE_JPEG_VALUE : image/jpeg (각 이미지 MIME TYPE)
  *
  * 반환형 byte[] 이유 : 파일은 바이트 단위 전송을 해야 꺠지지 않음
  * */
  @GetMapping(
      value = "/image",
      produces = MediaType.IMAGE_JPEG_VALUE)
  public byte[] getImage() throws IOException{
    return getClass()
        .getResourceAsStream("/images/병진이형오토바이.jpg")
        .readAllBytes();
  }

  /* 6. ResponseEntity 응답 */
  @GetMapping("/entity")
  public ResponseEntity<Message> getEntity(){
    return ResponseEntity.ok(new Message(200, "정상수행"));
  }
}
