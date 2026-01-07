/* root 계정 */
-- 1) 새로운 계정 만들기
CREATE USER 'swcamp'@'%' IDENTIFIED BY 'swcamp';

-- 2) 데이터베이스(스키마) 생성
CREATE DATABASE cqrs;

-- 3) 계정에 권한 부여
GRANT ALL PRIVILEGES
    ON cqrs.* TO 'swcamp'@'%';


/* swcamp - cqrs 스키마 */
CREATE TABLE IF NOT EXISTS tbl_category
(
    category_code    BIGINT AUTO_INCREMENT COMMENT '카테고리코드',
    category_name    VARCHAR(50) NOT NULL COMMENT '카테고리명',
    CONSTRAINT pk_category_code PRIMARY KEY (category_code)
) ENGINE=INNODB COMMENT '상품카테고리';

CREATE TABLE IF NOT EXISTS tbl_product
(
    product_code        BIGINT AUTO_INCREMENT COMMENT '상품코드',
    product_name        VARCHAR(100) NOT NULL COMMENT '상품명',
    product_price       VARCHAR(100) NOT NULL COMMENT '상품가격',
    product_description VARCHAR(1000) NOT NULL COMMENT '상품설명',
    category_code       BIGINT COMMENT '카테고리코드',
    product_image_url   VARCHAR(100) NOT NULL COMMENT '상품이미지경로',
    product_stock       BIGINT NOT NULL COMMENT '상품재고',
    created_at          DATETIME NOT NULL DEFAULT now() COMMENT '생성일시',
    modified_at         DATETIME NOT NULL DEFAULT now() COMMENT '수정일시',
    status              VARCHAR(10) NOT NULL DEFAULT 'USABLE' COMMENT '상태',
    CONSTRAINT pk_product_code PRIMARY KEY (product_code),
    CONSTRAINT fk_category_code FOREIGN KEY (category_code) REFERENCES tbl_category (category_code)
) ENGINE=INNODB COMMENT '상품';

INSERT INTO tbl_category (category_name)
VALUES ( '식사');
INSERT INTO tbl_category (category_name)
VALUES ('디저트');
INSERT INTO tbl_category (category_name)
VALUES ('음료');

INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('열무김치라떼', 4500, '열무로 만든 김치 라떼', 3, 'http://localhost:8080/productimgs/06a0060ae2da4dffb9a8a440ba5d9c5e.PNG', 10);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('우럭스무디', 5000, '우럭으로 만든 스무디', 3, 'http://localhost:8080/productimgs/fcb3e0c8f94940cf99724d26e6020259.PNG', 15);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('생갈치쉐이크', 6000, '생으로 갈아만든 갈치 쉐이크', 3, 'http://localhost:8080/productimgs/8e2492fd197e42d5855ffbbb5142b4ed.PNG', 17);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('갈릭미역파르페', 7000, '갈릭 미역을 섞어 만든 파르페', 2, 'http://localhost:8080/productimgs/58b3fd68f6074de2b33d4430fd29244b.PNG', 19);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('앙버터김치찜', 13000, '가장 먹을만한 김치찜', 1, 'http://localhost:8080/productimgs/7580adcf59d04240b7a16f6cf07bd34b.PNG', 19);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('생마늘샐러드', 12000, '생마늘을 넣어 만든 샐러드', 2, 'http://localhost:8080/productimgs/7b91aee3ddec49a69a9b7d2849493f7f.PNG', 24);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('민트미역국', 15000, '민트를 넣어만든 미역국', 1, 'http://localhost:8080/productimgs/af732dfe3e4f482bac8d1ef0bd9be02b.PNG', 12);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('한우딸기국밥', 20000, '한우에 딸기를 얹은 국밥', 1, 'http://localhost:8080/productimgs/8a4cd876df574970a565b41e47561080.PNG', 22);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('홍어마카롱', 9000, '홍어를 갈아 넣은 마카롱', 2, 'http://localhost:8080/productimgs/c0a177a658b44f749699f91a23c47d8b.PNG', 17);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('코다리마늘빵', 7000, '코다리 마늘빵은 진리', 2, 'http://localhost:8080/productimgs/053626c2d16f4814a5e81b842a115dc7.PNG', 17);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('정어리빙수', 10000, '정어리를 갈아 만든 빙수', 2, 'http://localhost:8080/productimgs/323a5df17163482d90a74f8198a4e4c6.PNG', 12);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('날치알스크류바', 2000, '스크류바에 날치알이라니', 2, 'http://localhost:8080/productimgs/d97144a76a7b42fabfcf8a5662762040.PNG', 12);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('직화구이젤라또', 8000, '젤라또를 직화로', 2, 'http://localhost:8080/productimgs/a4195b272f764a3ab81e85fbbba6c067.PNG', 16);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('과메기커틀릿', 13000, '커틀렛 속에 쏙 과메기', 1, 'http://localhost:8080/productimgs/702215dbe9784ebf92561d5504b0b5a6.PNG', 11);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('죽방멸치튀김우동', 11000, '너도 나도 죽방 멸치 우동', 1, 'http://localhost:8080/productimgs/5c226e59e7c342608d220ce0c476d01f.PNG', 13);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('흑마늘아메리카노', 3000, '흑마늘을 넣은 아메아메아메리카노', 3, 'http://localhost:8080/productimgs/684fd2cccfc74ad4944619bc72f76788.PNG', 21);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('아이스가리비관자육수', 50000, '아이스 육수 가리비 관자', 1, 'http://localhost:8080/productimgs/b150ff66223a46adabb75a71299cb25a.PNG', 16);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('붕어빵초밥', 20000, '초밥이 붕어빵에 있는것인가 그 반대인가', 1, 'http://localhost:8080/productimgs/c260a42b01394faba92ed9a7ca868aa9.PNG', 11);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('까나리코코넛쥬스', 5000, '코코넛 주스에 까나리 한스푼', 3, 'http://localhost:8080/productimgs/21406eb90b2f4ae09ea0af015d2df6fe.PNG', 28);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('마라깐쇼한라봉', 22000, '마라깐쇼 한라봉 고고', 3, 'http://localhost:8080/productimgs/eccf4c58d72241ddb4c84401485d4363.png', 21);
INSERT INTO tbl_product (product_name, product_price, product_description, category_code, product_image_url, product_stock)
VALUES ('돌미나리백설기', 5000, '백설기 속에 씹히는 돌미나리', 2, 'http://localhost:8080/productimgs/9a6c1b13af0a469fa2d9e3084f6e438c.PNG', 12);

COMMIT;
