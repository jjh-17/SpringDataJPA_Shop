package jpabook.jpashop.domain.product;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("movie") //DB에 저장할 때 구분하기 위함
@Getter @Setter
public class Movie extends Product {
    private String director;
    private String actor;
}
