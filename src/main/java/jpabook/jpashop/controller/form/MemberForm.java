package jpabook.jpashop.controller.form;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    //그 외 나머지 변수는 필수가 아님
    private String city;
    private String street;
    private String zipcode;

}
