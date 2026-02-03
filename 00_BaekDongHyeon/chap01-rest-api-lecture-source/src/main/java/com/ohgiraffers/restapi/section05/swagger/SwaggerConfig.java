package com.ohgiraffers.restapi.section05.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Bean 등록 + 설정(내부 메서드 전부 실행)
public class SwaggerConfig {

  /* Swagger 문서 생성 설정 */
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().info(swaggerInfo());
  }

  /* API 문서 기본 정보 설정
   * */
  private Info swaggerInfo() {
    return new Info()
        .title("Ohgiraffers API")
        .description("SpringBoot Swagger 연동 테스트")
        .version("1.0.0");
  }

}