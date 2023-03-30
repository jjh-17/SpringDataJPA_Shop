package jpabook.jpashop.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    //엘범
    private String artist;
    private String etc;

    //책
    private String author;
    private String isbn;

    //영화
    private String actor;
    private String director;
}
