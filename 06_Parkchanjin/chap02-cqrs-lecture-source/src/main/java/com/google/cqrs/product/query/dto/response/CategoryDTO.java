package com.google.cqrs.product.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
  private Long categoryCode;
  private String categoryName; private Long productCode;
  private String productName;
  private Long productPrice;
  private String productDescription;
  private CategoryDTO category;
  private String productImageUrl;
  private Long productStock;
}
