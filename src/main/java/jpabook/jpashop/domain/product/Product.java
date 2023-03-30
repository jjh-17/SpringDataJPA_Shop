package jpabook.jpashop.domain.product;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //Album, Book, Movie를 모두 한 테이블에 저장한다.
@DiscriminatorColumn(name = "dtype") //하위 클래스를 구분하는 용도의 컬럼
@Getter @Setter
public class Product {
    @Id @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    //Category의 products 필드와 매핑됨 ==> categories는 읽기 전용 필드
    //list와 같은 Collection은 필드에서 초기화 하는 것이 안전
    //  ==> null 문제 방지
    //      하이버네이트는 엔티티를 영속화하면서 컬렉션을 감싸 하이버네이트 내장 컬렉션으로 변경.
    //      임의의 메서드에서 컬렉션을 잘못 생성할 시 하이버네이트 내부 메커니즘에 문제 발생 가능
    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    //비지니스 로직 - 스톡 증가
    public void addStock(int quantity) {
        setStockQuantity(getStockQuantity() + quantity);
    }

    //비지니스 로직 - 스톡 감소
    public void subtractStock(int quantity) {
        int rest = getStockQuantity() - quantity;
        if (rest < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        setStockQuantity(rest);
    }
}
