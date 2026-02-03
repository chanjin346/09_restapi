package com.google.cqrs.product.domain.repository;

import com.google.cqrs.product.domain.aggregate.Product;

import java.util.Optional;

public interface ProductRepository {

  Product save(Product product);

  Optional<Product> findById(Long ProductCode);

  void deleteById(Long productCode);
}
