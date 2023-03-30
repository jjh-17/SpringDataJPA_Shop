package jpabook.jpashop.service;

import jakarta.transaction.Transactional;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.product.Product;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Fail.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProductServiceTest {
    @Autowired ProductService productService;
    @Autowired ProductRepository productRepository;

    @Test
    public void 상품추가() throws Exception {
        //given
        Product product = new Product();
        product.setName("콘칩");

        //when
        Long savedId = productService.saveProduct(product);

        //then
        Assertions.assertThat(product).isEqualTo(productRepository.findProductById(savedId));
    }
}