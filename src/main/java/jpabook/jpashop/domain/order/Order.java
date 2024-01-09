package jpabook.jpashop.domain.order;

import jakarta.persistence.*;
import jpabook.jpashop.domain.delivery.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.delivery.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //생성자를 protected로 선언하여 다른 클래스 내에서 임의로 Order 인스턴스를 생성하지 못하도록 함

public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //지연 로딩 사용 ==> SQL 실행 추적 쉬움, JPQL 사용 시 N+1 문제 발생 방지
    @ManyToOne(fetch = FetchType.LAZY)      //Member와의 관계 : 다대일.
    @JoinColumn(name = "member_id")         //Member와 Join시 "member_id"를 기준으로 join
    private Member member;

//    OrderProduct의 order 필드에 대하여 매핑됨 ==> Order가 달라져야 order_id가 달라진다.
//    list와 같은 Collection은 필드에서 초기화 하는 것이 안전
//      ==> null 문제 방지
//          하이버네이트는 엔티티를 영속화하면서 컬렉션을 감싸 하이버네이트 내장 컬렉션으로 변경.
//          임의의 메서드에서 컬렉션을 잘못 생성할 시 하이버네이트 내부 메커니즘에 문제 발생 가능
//    CascadeType.ALL : persist 전파. Order 변수를 persist하면 orderProducts 변수도 자동으로 persist된다.
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    //Order 변수를 persist하면 Delivery 변수도 자동으로 persist된다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;          //배송 정보

    private LocalDateTime orderDate;    //주문 시각

    @Enumerated(EnumType.STRING)
    private OrderStatus status;         //주문 상태

//    연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //생성 메서드 ==> 생성과정에서 수정이 필요하면 이 친구만 수정하면 된다!. 생성자를 대체한다.
    public static Order createOrder(Member member, Delivery delivery, OrderProduct... orderProducts) {
        //주문 생성
        Order order = new Order();

        order.setMember(member); //
        order.setDelivery(delivery);
        for (OrderProduct orderProduct : orderProducts) {
            order.addOrderProduct(orderProduct);
        }
        order.setStatus(OrderStatus.ORDER); //초기엔 ORDER로 초기화
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    //비지니스 로직 - 주문 취소
    public void cancelOrder() {
        if (delivery.getStatus() == DeliveryStatus.COMPLETE) {
            throw new IllegalStateException("배송이 완료된 상품은 취소할 수 없습니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderProduct orderProduct : orderProducts) { //모든 주문 상품에 대하여 원상 복귀 진행
            orderProduct.cancelOrder();
        }
    }

    //조회 로직 - 전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderProduct orderProduct : orderProducts) {
            totalPrice += orderProduct.getTotalPrice();
        }

        return totalPrice;

        /*
        //대체 가능
        return orderProducts.stream()
                .mapToInt(OrderProduct::getTotalPrice)
                .sum();
         */
    }
}
