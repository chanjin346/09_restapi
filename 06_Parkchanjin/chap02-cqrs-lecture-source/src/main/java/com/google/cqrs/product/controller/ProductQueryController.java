package com.google.cqrs.product.controller;

import com.google.cqrs.common.dto.ApiResponse;
import com.google.cqrs.product.query.dto.request.ProductSearchRequest;
import com.google.cqrs.product.query.dto.response.ProductDetailResponse;
import com.google.cqrs.product.query.dto.response.ProductListResponse;
import com.google.cqrs.product.query.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/* 조회 담당 컨트롤러 (Query Side) */
@RestController // @ResponseBody + @Controller
@RequiredArgsConstructor
public class ProductQueryController {

  private final ProductQueryService productQueryService;

  @GetMapping("/products/{productCode}")
  public ResponseEntity<ApiResponse<ProductDetailResponse>> getProduct(@PathVariable("productCode") Long productCode) {
    ProductDetailResponse response =
    productQueryService.getProduct(productCode);

    return ResponseEntity.ok(ApiResponse.success(response)); // 항상 일관된 값을 제공함(사용자 편의성)
  }

  /* 조건에 맞는 상품 목록 조회 */
  @GetMapping("/products")
  public ResponseEntity<ApiResponse<ProductListResponse>> getProducts(
     // @ModelAttribute (생략 가능, 값이 여러개일때 사용) @RequestParam (값이 하나일때 사용)
     ProductSearchRequest productSearchRequest /* 커맨드 객체 */
  ){
    ProductListResponse response
        = productQueryService.getProducts(productSearchRequest);

    return ResponseEntity.ok(ApiResponse.success(response));
  }

}
