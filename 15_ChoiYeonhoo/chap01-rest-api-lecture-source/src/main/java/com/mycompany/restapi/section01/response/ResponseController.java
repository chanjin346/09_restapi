package com.mycompany.restapi.section01.response;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/* HttpMessageConverter
* - Spring에서 Http 요청/응답을 알맞은 형태로 자동 변환해주는 객체
* - 숫자, 문자열 -> text/plain
* - Object 타입 -> Application/json
* - 파일        -> 지정된 produces MIME Type
* - */

@RestController // @ResponseBody + Controller
                // 모든 Handler Method가 data로만 응답을 하는 컨트롤러
@RequestMapping("/response")
public class ResponseController {

  /* 1. 문자열 응답 */
  @GetMapping("/hello")
//  @ResponseBody // View Resolver를 찾아가지 않고, Response Body에 담겨 그대로 Client에 전달
  public String helloWorld(){
    return "helloWorld!";
  }

  /* 2.Object 응답 */
  @GetMapping("/message")
  public Message getMessage(){
    Message message = new Message(200,"메시지를 응답합니다");
    return message;
  }

  /* 3. List 응답 */
  @GetMapping("/list")
  public List<String> getList(){
    return List.of(new String[] {"사과","딸기","바나나"});
  }

  /* 4. Map 응답 */
  @GetMapping("/map")
  public Map<Integer, String> getMap() {

    List<Message> messageList = new ArrayList<>();
    messageList.add(new Message(200, "정상 응답"));
    messageList.add(new Message(404, "페이지를 찾을 수 없습니다"));
    messageList.add(new Message(500, "개발자의 잘못입니다"));

    return messageList.stream()
        .collect(Collectors.toMap(Message::getHttpStatusCode, Message::getMessage));

  }

  /* 5. File 응답
  * produces : 해당 api의 응답 데이터 타입을 명시하는 속성
  *
  * MediaType.IMAGE_PNG_VALUE : image/png (각 이미지 MIME TYPE)
  *
  * 반환형 byte[] 이유 : 파일은 byte 단위 전송을 해야 깨지지 않음
  * */
  @GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
  public byte[] getImage() throws IOException{
    return getClass().getResourceAsStream("/images/image.png").readAllBytes();
  }

  /* 6. ResponseEntity 응답 */
  @GetMapping("/entity")
  public ResponseEntity<Message> getEntity(){
    return ResponseEntity.ok(new Message(200,"정상 수행")); // .ok : 200번 .nofound : 404번 등..
  }
}
