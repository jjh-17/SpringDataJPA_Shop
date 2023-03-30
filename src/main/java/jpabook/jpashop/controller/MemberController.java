package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.controller.form.MemberForm;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원가입 창을 연다
    @GetMapping("/members/new")
    public String createMemberForm(Model model) {
        //controller에서 view로 넘어갈 때, MemberForm 데이터를 넘긴다
        model.addAttribute("memberForm", new MemberForm());

        return "members/createMemberForm";
    }

    //회원가입 창에서 입력한 정보를 저장한다.
    @PostMapping("/members/new")
    public String createMember(@Valid MemberForm memberForm, BindingResult bindingResult) { //@Valid : MemberForm 클래스 내 @인식

        //bindingResult에 페이지내 작업을 수행한 결과가 담겨 있다.
        if (bindingResult.hasErrors()) { //작업에 오류가 있는 경우 해당 오류를 표현해주며 다시 초기 창을 연다.
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/"; //첫 페이지로 이동
    }

    //회원 목록 창
    @GetMapping("/members")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }
}
