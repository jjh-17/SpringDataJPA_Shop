package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //Order와의 관계 : 일대다
    //Order에 있는 member 필드에 대하여 매핑되었다 ==> member가 바뀌면 그에 따라 member_id(포렌키) 값이 달라진다.
    //list와 같은 Collection은 필드에서 초기화 하는 것이 안전
    //  ==> null 문제 방지
    //      하이버네이트는 엔티티를 영속화하면서 컬렉션을 감싸 하이버네이트 내장 컬렉션으로 변경.
    //      임의의 메서드에서 컬렉션을 잘못 생성할 시 하이버네이트 내부 메커니즘에 문제 발생 가능
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
