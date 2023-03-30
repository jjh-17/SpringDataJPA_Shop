package jpabook.jpashop.domain.order;

import jakarta.persistence.*;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_product")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //생성자를 protected로 선언하여 다른 클래스 내에서 임의로 OrderProduct 인스턴스를 생성하지 못하도록 함
public class OrderProduct {
    @Id @GeneratedValue
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //하나의 Product는 여러 개의 OrderProduct를 가진다.
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY) //하나의 Order는 여러 개의 OrderProduct를 가진다
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격
    private int count; //주문 수량

    //생성 메서드 : 상품, 주문 가격, 수량
    public static OrderProduct createOrderProduct(Product product, int orderPrice, int count) {
        OrderProduct orderProduct = new OrderProduct();

        //상품 주문 정보 초기화
        orderProduct.setProduct(product);
        orderProduct.setOrderPrice(orderPrice);
        orderProduct.setCount(count);

        //상품의 수량 감소
        product.subtractStock(count);

        return orderProduct;
    }

    //비지니스 로직 - 주문 취소
    public void cancelOrder() {
        getProduct().addStock(count); //재고 수량을 원상 복귀
    }

    //조회 로직 - 총 주문 가격
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
