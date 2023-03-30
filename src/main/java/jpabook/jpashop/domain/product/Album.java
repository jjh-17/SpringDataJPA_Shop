package jpabook.jpashop.domain.product;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("album") //DB에 저장할 때 구분하기 위함
@Getter @Setter
public class Album extends Product {
    private String artist;
    private String etc;
}
