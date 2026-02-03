package com.ohgiraffers.cqrs.product.command.application.service;

import com.ohgiraffers.cqrs.common.service.FileStorageService;
import com.ohgiraffers.cqrs.exception.BusinessException;
import com.ohgiraffers.cqrs.exception.ErrorCode;
import com.ohgiraffers.cqrs.product.command.application.dto.request.ProductCreateRequest;
import com.ohgiraffers.cqrs.product.command.application.dto.request.ProductUpdateRequest;
import com.ohgiraffers.cqrs.product.command.domain.aggregate.Product;
import com.ohgiraffers.cqrs.product.command.domain.aggregate.ProductStatus;
import com.ohgiraffers.cqrs.product.command.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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


  /* 상품 수정 */
  @Transactional
  public void updateProduct(
      Long productCode,
      ProductUpdateRequest productUpdateRequest,
      MultipartFile productImg
  ){

    // JPA를 이용한 DB 수정 방법
    // 1) 수정할 데이터를 조회 -> 조회된 Entity(영속 상태)
    // 2) 조회된 Entity 수정 -> 더티 체킹(변화 감지)
    // 3) Commit -> 수정된 내용이 DB에 반영


    /* 조회 성공 시 Optional 벗겨서 Product로 반환
    * 조회 실패 시 예외 강제 발생*/
    Product product
        = productRepository.findById(productCode)
        .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

    /* 수정 요청에 파일이 전달되서 올 경우 */
    if(productImg != null){

      // 새 파일 저장
      String replaceFileName = fileStorageService.storeFile(productImg);

      // URL + 기존 이미지 파일명 얻어오기
      String oldFileName = product.getProductImageUrl();

      // URL 제거
      oldFileName = oldFileName.replace(IMAGE_URL, "");

      // 기존 파일 삭제
      fileStorageService.deleteFile(oldFileName);

      // 엔티티 파일 URL 변경
      product.changeProductImageUrl(replaceFileName);
    }


    /* 조회된 Entity를 수정 */
    product.updateProductDetails(
        productUpdateRequest.getProductName(),
        productUpdateRequest.getProductPrice(),
        productUpdateRequest.getProductDescription(),
        productUpdateRequest.getCategoryCode(),
        productUpdateRequest.getProductStock(),
        ProductStatus.valueOf(productUpdateRequest.getStatus())
    );
  }


  /* 상품 삭제 */
  @Transactional
  public void deleteProduct(Long productCode){
    /* 실제 데이터를 삭제하지 않고 상태 값을 변경하거나
    *  삭제 일시를 기록하는 Soft Delete 방식을 사용.
    *
    *  Product Entity에 정의된 @SQLDelete 어노테이션에 의해
    *  delete 메서드 호출 시 자동으로 update 구문이 수행된다.
    * */

    productRepository.deleteById(productCode);
  }

}
