package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

//    list와 같은 Collection은 필드에서 초기화 하는 것이 안전
//      ==> null 문제 방지
//          하이버네이트는 엔티티를 영속화하면서 컬렉션을 감싸 하이버네이트 내장 컬렉션으로 변경.
//          임의의 메서드에서 컬렉션을 잘못 생성할 시 하이버네이트 내부 메커니즘에 문제 발생 가능
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(                                                     //다대다 관계는 중간 테이블로 연결을 해야한다.
            name = "category_product",                              //중간 테이블 이름
            joinColumns = @JoinColumn(name = "category_id"),        //중간 테이블에 있는 id
            inverseJoinColumns = @JoinColumn(name = "product_id"))  //중간 테이블에서 Product로 접근하기 위한 콜룸 명
    private List<Product> products = new ArrayList<>();

//    스스로 양방향 연관관계를 걸음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

//    list와 같은 Collection은 필드에서 초기화 하는 것이 안전
//      ==> null 문제 방지
//          하이버네이트는 엔티티를 영속화하면서 컬렉션을 감싸 하이버네이트 내장 컬렉션으로 변경.
//          임의의 메서드에서 컬렉션을 잘못 생성할 시 하이버네이트 내부 메커니즘에 문제 발생 가능
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Category> child = new ArrayList<>();

    //연관관계 메서드
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
