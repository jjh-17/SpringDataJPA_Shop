package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager entityManager;

    //상품 추가
    public void saveProduct(Product product) {
        if (product.getId() == null) {      //새로운 product를 저장
            entityManager.persist(product);
        } else {                            //기존 product 수정
            entityManager.merge(product); //병합 : 모든 필드를 변경하므로 조심히 사용할 것 ==> 엔티티 수정시 사용
        }
    }

    //상품 아이디로 검색
    public Product findProductById(Long id) {
        return entityManager.find(Product.class, id);
    }

    //모든 상품 조회
    public List<Product> findProductAll() {
        return entityManager
                .createQuery("select p from Product p", Product.class)
                .getResultList();
    }
}
