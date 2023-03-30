package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.product.Product;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import jpabook.jpashop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ProductService productService;

    @GetMapping("/order")
    public String createOrderForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Product> products = productService.findProducts();

        model.addAttribute("members", members);
        model.addAttribute("products", products);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String createOrder(@RequestParam("memberId") Long memberId,
                              @RequestParam("productId") Long productId,
                              @RequestParam("count") int count) {

        orderService.order(memberId, productId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);

        return "redirect:/orders";
    }
}
