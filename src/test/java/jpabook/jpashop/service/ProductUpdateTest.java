package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.product.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductUpdateTest {
    @Autowired
    EntityManager entityManager;

    @Test
    public void updateTest() {
        Book book = entityManager.find(Book.class, 1L);

        //Tr
    }
}
