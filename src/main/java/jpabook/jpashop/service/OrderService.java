package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.delivery.Delivery;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderProduct;
import jpabook.jpashop.domain.product.Product;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;      //
    private final MemberRepository memberRepository;    //회원의 아이디를 이용한 상품 주문용
    private final ProductRepository productRepository;


    //주문 - memberId 회원이 productID 상품을 count만큼 구매
    @Transactional
    public Long order(Long memberId, Long productId, int count) {
        //주문하는 회원, 주문할 상품 정보 조회
        Member member = memberRepository.findMemberById(memberId);
        Product product = productRepository.findProductById(productId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, product.getPrice(), count);

        //주문 생성 및 저장
        Order order = Order.createOrder(member, delivery, orderProduct);
        orderRepository.saveOrder(order); //cascade에 의해 delivery, orderProduct는 자동으로 persist됨

        return order.getId();
    }

    //취소 - orderId 주문을 취소한다.
    @Transactional
    public void cancelOrder(Long orderId) {
        //Order 정보 조회
        Order order = orderRepository.findOrderById(orderId);

        //주문 취소
        order.cancelOrder();
    }


    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }

}
