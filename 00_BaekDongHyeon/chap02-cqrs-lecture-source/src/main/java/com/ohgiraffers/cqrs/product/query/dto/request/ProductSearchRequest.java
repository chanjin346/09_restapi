package com.ohgiraffers.cqrs.product.query.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {
  private Integer page = 1;
  private Integer size = 10;
  private Long categoryCode;
  private String productName;

  // 건너 뛸 행의 개수
  public int getOffset() {
    return (page - 1) * size;
  }

  // 조회할 행의 개수
  public int getLimit() {
    return size;
  }
}