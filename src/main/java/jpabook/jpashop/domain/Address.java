package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable //
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {}

    //변경이 불가능하도록 setter를 생성자로 대체
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
