package com.ohgiraffers.restapi.section02.responseentity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController // @ResponseBody + @Controller
@RequestMapping("/entity")
public class ResponseEntityController {
	
	private List<UserDTO> users;
	
	public ResponseEntityController() {
		users = new ArrayList<>();
		
		users.add(
			new UserDTO(1, "user01", "pass01", "홍길동", new java.util.Date())
		);
		users.add(
			new UserDTO(2, "user02", "pass02", "유관순", new java.util.Date())
		);
		users.add(
			new UserDTO(3, "user03", "pass03", "이순신", new java.util.Date())
		);
		
	}

  /* 1. 유저 목록 조회 (GET - 조회 용도)
  * - contentType 기본값 : application/json
  *   -> 필요 시 변경
  * */
  @GetMapping("/users")
  public ResponseEntity<ResponseMessage> findAllUsers(){
    /* 1) 응답 헤어 설정 */
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(
        new MediaType(
            "application",
            "json",
            StandardCharsets.UTF_8)
    );

    /* 2) 응답 바디 설정
     * - 응답 시 전달할 값(데이터)
     * */
    Map<String,Object> responseMap = new HashMap<>();
    responseMap.put("users", users);

    // 3) 응답 메시지 설정
    ResponseMessage responseMessage
        = new ResponseMessage(
        200,
        "조회 성공",
        responseMap);

    /* 4. ResponseEntity 객체를 생성해서 반환 */
    /*    return new ResponseEntity<>(
        responseMessage,
        httpHeaders,
        HttpStatus.OK);*/

    // 더 많이 쓰이는 방식
    /* (추천) ResponseEntity 정적 메서드 방식(빌더 패턴) */
    return ResponseEntity
        .ok()
        .headers(httpHeaders)
        .body(responseMessage);
  }

  /* 2. 특정 유저 조회 */
  @GetMapping("/users/{userNo}")
  public ResponseEntity<ResponseMessage> findUserByNo(
      @PathVariable("userNo") int userNo
  ){
    /* 1) 응답 헤어 설정 */
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(
        new MediaType(
            "application",
            "json",
            StandardCharsets.UTF_8)
    );

   /* *//* 2) 응답 바디 설정 *//*
    Map<String, Object> responseMap = new HashMap<>();

    UserDTO foundUser = users.stream()
        .filter(user -> user.getNo() == userNo).findFirst().get();

    return ResponseEntity
        .ok()
        .headers(httpHeaders)
        .body(new ResponseMessage(200,
            "조회 성공", responseMap));
  }*/

    /* 2+a. 예외 상황 추가 */ // 이건 참고만 ㄱㄱ
    return users.stream()
        .filter(user -> user.getNo() == userNo)
        .findFirst()
        .map(user -> {
          responseMap.put("user", user);
          return ResponseEntity
              .ok()
              .headers(httpHeaders)
              .body(new ResponseMessage(
                  200, "조회 성공", responseMap));
        }).orElseGet(() -> ResponseEntity.notFound().build()); // 404라는 상태만 전달 // 없으면 404
        }
    Map<String, Object> responseMap = new HashMap<>();


  /* 3. 유저 등록 (POST)
  *  @RequestBody :
  *  - 요청 바디에 담긴 데이터를 꺼내는 어노테이션
   * - 옆에 작성된 DTO(필드명 == key 일치) 또는 Map에 데이터를 저장
  *  */ // DTO 저장 조건 필드명 == KEY값이 일치해야 대입이 됨. Map은 있는 그대로 다 넘김
  @PostMapping("/users")
  public ResponseEntity<Void> registUser(@RequestBody UserDTO newUser) {

    System.out.println(newUser);

    // 마지막 User의 번호를 얻어와 + 1
    int lastUserNo = users.get(users.size() - 1).getNo();
    newUser.setNo(lastUserNo + 1);
    newUser.setEnrollDate(new java.util.Date());

    users.add(newUser);

    return ResponseEntity
        .created(
            URI.create("/entity/users/" + users.get(users.size() - 1).getNo())
        )
        .build();
  }

  /* 4. 유저 수정하기 */
  @PutMapping("/users/{userNo}")
  public ResponseEntity<Void> modifyUser(
      @PathVariable int userNo, @RequestBody UserDTO modifyInfo // 몇 번 회원 바꾸고 싶은지. 바꾸고 싶은 정보
  ) {

    System.out.println(modifyInfo);

    UserDTO foundUser
        = users.stream().filter(user -> user.getNo() == userNo)
        .collect(Collectors.toList()).get(0); // 찾은 거 중에 첫번째
    foundUser.setId(modifyInfo.getId());
    foundUser.setPwd(modifyInfo.getPwd());
    foundUser.setName(modifyInfo.getName());

    return ResponseEntity
        .created(URI.create("/entity/users/" + userNo))
        .build();
  }

  /* 5. 유저 삭제 */
  @DeleteMapping("/users/{userNo}")
  public ResponseEntity<?> removeUser(@PathVariable int userNo) {

    UserDTO foundUser
        = users.stream().filter(user -> user.getNo() == userNo)
        .collect(Collectors.toList()).get(0);

    users.remove(foundUser);

    return ResponseEntity
        .noContent() //내용 없음
        .build();
  }
}