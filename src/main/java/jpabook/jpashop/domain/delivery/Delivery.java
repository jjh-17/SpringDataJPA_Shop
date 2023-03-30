package jpabook.jpashop.domain.delivery;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter //실무에서는 가급적 Setter는 지양해야한다. 오류 찾기가 힘들다.
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    //OneToOne 이면 어느 쪽으로 매핑을 하든 상관 없으나, 포렌키를 한 곳으로 모으는 방향으로 하는 것이 좋음
    //Order의 delivery 필드에 대하여 매핑됨 ==> order 필드는 읽기 전용 필드
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
