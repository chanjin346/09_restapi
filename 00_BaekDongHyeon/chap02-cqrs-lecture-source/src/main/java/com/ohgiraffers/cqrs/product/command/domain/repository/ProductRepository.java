package com.ohgiraffers.cqrs.product.command.domain.repository;

import com.ohgiraffers.cqrs.product.command.domain.aggregate.Product;

public interface ProductRepository {

  Product save(Product product);
}
