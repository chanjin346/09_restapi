package com.ohgiraffers.cqrs.product.command.application.service;

import com.ohgiraffers.cqrs.common.service.FileStorageService;
import com.ohgiraffers.cqrs.product.command.application.dto.request.ProductCreateRequest;
import com.ohgiraffers.cqrs.product.command.domain.aggregate.Product;
import com.ohgiraffers.cqrs.product.command.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductCommandService {

  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;
  private final FileStorageService fileStorageService;

  @Value("${image.image-url}")
  private String IMAGE_URL; // http://localhost:8080/productimgs/

  /* 상품 등록 */
  @Transactional
  public Long createProduct(
      ProductCreateRequest productCreateRequest,
      MultipartFile productImg){

    /* 전달 받은 파일을 저장 */
    String replaceFileName = fileStorageService.storeFile(productImg);

    /* DTO -> Entity 변환 */
    Product newProduct
        = modelMapper.map(productCreateRequest, Product.class);

    /* 이미지 경로 추가 */
    newProduct.changeProductImageUrl(IMAGE_URL + replaceFileName);


    // newProduct : 비영속 상태
    // productRepository.save() 반환값 product : 영속 상태
    //  -> Auto Increment로 생성된 productCode가 담겨 있음
    Product product = productRepository.save(newProduct);



    // Product 삽입 후 반환된 productCode를 결과로 반환
    return product.getProductCode();
  }
}
