package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager entityManager;

    public void saveOrder(Order order) {
        entityManager.persist(order);
    }

    public Order findOrderById(Long id) {
        return entityManager.find(Order.class, id);
    }

    //회원명, 주문상태에 따른 검색 ==> JPQL로 하나하나 설정(비추천)
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o from Order o Join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
        }

        //회원이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }

            jpql += " m.name like :name";
        }

        TypedQuery<Order> typedQuery = entityManager.createQuery(jpql, Order.class)
//                .setFirstResult(100) //페이징 기법 ==> 한번에 100개씩 show
                .setMaxResults(1000); //최대 1000개 show

        if (orderSearch.getOrderStatus() != null) {
            typedQuery = typedQuery.setParameter("status", OrderSearch.class);
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            typedQuery = typedQuery.setParameter("name", orderSearch.getMemberName());
        }

        return typedQuery.getResultList();
    }

    //회원명, 주문상태에 따른 검색 ==> JPA Criteria(비추천)
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        Join<Order, Member> join = root.join("member", JoinType.INNER);
        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = criteriaBuilder.equal(root.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = criteriaBuilder.like(join.<String>get("name"),
                    "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> typedQuery = entityManager.createQuery(criteriaQuery)
                .setMaxResults(1000);

        return typedQuery.getResultList();
    }

    //회원명, 주문상태에 따른 검색 ==> Querydsl(추천)
//    public List<Order> findAllByQuerydsl(OrderSearch orderSearch) {
//
//    }


}
