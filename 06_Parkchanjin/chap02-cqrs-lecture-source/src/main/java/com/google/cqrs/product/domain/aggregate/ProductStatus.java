package com.google.cqrs.product.domain.aggregate;

public enum ProductStatus {
  USABLE,   // 주문 가능
  DISABLE,  // 주문 불가
  DELETE    // 삭제(soft delete)
}
