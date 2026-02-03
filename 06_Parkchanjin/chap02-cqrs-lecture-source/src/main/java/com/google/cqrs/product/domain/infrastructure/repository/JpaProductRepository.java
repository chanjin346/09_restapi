package com.google.cqrs.product.domain.infrastructure.repository;

import com.google.cqrs.product.domain.aggregate.Product;
import com.google.cqrs.product.domain.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long>, ProductRepository {


}
