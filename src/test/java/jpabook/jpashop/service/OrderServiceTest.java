package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderStatus;
import jpabook.jpashop.domain.product.Album;
import jpabook.jpashop.domain.product.Book;
import jpabook.jpashop.domain.product.Movie;
import jpabook.jpashop.domain.product.Product;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager entityManager;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    final String book_name = "jpa 활용 책", album_name = "jpa 활용 엘범", movie_name = "jpa 활용 영화";
    final int book_price = 10000, album_price = 6000, movie_price = 14000;
    final int book_quantity = 20, album_quantity = 10, movie_quantity = 15;

    private Product createMovie(String movie_name, int movie_price, int movie_quantity) {
        Product movie = new Movie();

        movie.setName(movie_name);
        movie.setPrice(movie_price);
        movie.setStockQuantity(movie_quantity);
        entityManager.persist(movie);

        return movie;
    }

    private Product createAlbum(String album_name, int album_price, int album_quantity) {
        Product album = new Album();

        album.setName(album_name);
        album.setPrice(album_price);
        album.setStockQuantity(album_quantity);
        entityManager.persist(album);

        return album;
    }

    private Product createBook(String book_name, int book_price, int book_quantity) {
        Product book = new Book();

        book.setName(book_name);
        book.setPrice(book_price);
        book.setStockQuantity(book_quantity);
        entityManager.persist(book);

        return book;
    }

    private Member createMember() {
        Member member = new Member();

        member.setName("회원1");
        member.setAddress(new Address("경기", "A도로", "123456"));
        entityManager.persist(member);

        return member;
    }

    @Test
    public void 상품주문() throws Exception {
        final int book_count = 1, album_count = 10, movie_count = 14;

        //given
        Member member = createMember();
        Product book = createBook(book_name, book_price, book_quantity);
        Product album = createAlbum(album_name, album_price, album_quantity);
        Product movie = createMovie(movie_name, movie_price, movie_quantity);

        //when
        Long book_order_id = orderService.order(member.getId(), book.getId(), book_count);
        Long album_order_id = orderService.order(member.getId(), album.getId(), album_count);
        Long movie_order_id = orderService.order(member.getId(), movie.getId(), movie_count);

        //then
        Order get_book_order = orderRepository.findOrderById(book_order_id);
        Order get_album_order = orderRepository.findOrderById(album_order_id);
        Order get_movie_order = orderRepository.findOrderById(movie_order_id);

        //주문한 상품의 상태 확인
        assertEquals("책의 상품 주문 상태는 Order", OrderStatus.ORDER, get_book_order.getStatus());
        assertEquals("엘범의 상품 주문 상태는 Order", OrderStatus.ORDER, get_album_order.getStatus());
        assertEquals("영화의 상품 주문 상태는 Order", OrderStatus.ORDER, get_movie_order.getStatus());

        //주문한 상품의 종류 수 확인
        assertEquals("주문한 책의 종류 수는 정확하다", 1, get_book_order.getOrderProducts().size());
        assertEquals("주문한 엘범의 종류 수는 정확하다", 1, get_album_order.getOrderProducts().size());
        assertEquals("주문한 영화의 종류 수는 정확하다", 1, get_movie_order.getOrderProducts().size());

        assertEquals("책의 총 가격은 정확해야한다.", book_price * book_count, get_book_order.getTotalPrice());
        assertEquals("엘범의 총 가격은 정확해야한다.", album_price * album_count, get_album_order.getTotalPrice());
        assertEquals("영화의 총 가격은 정확해야한다.", movie_price * movie_count, get_movie_order.getTotalPrice());

        assertEquals("책의 주문 수량만큼 재고가 줄어야 한다.", book_quantity - book_count, book.getStockQuantity());
        assertEquals("엘범의 주문 수량만큼 재고가 줄어야 한다.", album_quantity - album_count, album.getStockQuantity());
        assertEquals("영화의 주문 수량만큼 재고가 줄어야 한다.", movie_quantity - movie_count, movie.getStockQuantity());
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        final int book_count = 1, album_count = 10, movie_count = 14;

        Member member = createMember();
        Product book = createBook(book_name, book_price, book_quantity);
        Product album = createAlbum(album_name, album_price, album_quantity);
        Product movie = createMovie(movie_name, movie_price, movie_quantity);

        Long book_order_id = orderService.order(member.getId(), book.getId(), book_count);
        Long album_order_id = orderService.order(member.getId(), album.getId(), album_count);
        Long movie_order_id = orderService.order(member.getId(), movie.getId(), movie_count);

        //when
        orderService.cancelOrder(book_order_id);
        orderService.cancelOrder(album_order_id);
        orderService.cancelOrder(movie_order_id);

        //then
        Order get_book_order = orderRepository.findOrderById(book_order_id);
        Order get_album_order = orderRepository.findOrderById(album_order_id);
        Order get_movie_order = orderRepository.findOrderById(movie_order_id);

        assertEquals("책의 주문 취소 상태는 CANCEL", OrderStatus.CANCEL, get_book_order.getStatus());
        assertEquals("주문 취소된 책의 재고는 원상복귀 되어야 한다.", book_quantity, book.getStockQuantity());

        assertEquals("엘범의 주문 취소 상태는 CANCEL", OrderStatus.CANCEL, get_album_order.getStatus());
        assertEquals("주문 취소된 엘범의 재고는 원상복귀 되어야 한다.", album_quantity, album.getStockQuantity());

        assertEquals("영화의 주문 취소 상태는 CANCEL", OrderStatus.CANCEL, get_movie_order.getStatus());
        assertEquals("주문 취소된 영화의 재고는 원상복귀 되어야 한다.", movie_quantity, movie.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 재고수량초과1() throws Exception {
        //given
        final int book_count = 30;

        Member member = createMember();
        Product book = createBook(book_name, book_price, book_quantity);

        //when
        orderService.order(member.getId(), book.getId(), book_count);

        //then
        fail("책의 재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test(expected = NotEnoughStockException.class)
    public void 재고수량초과2() throws Exception {
        //given
        final int album_count = 233321;

        Member member = createMember();
        Product album = createAlbum(album_name, album_price, album_quantity);

        //when
        orderService.order(member.getId(), album.getId(), album_count);

        //then
        fail("엘범의 재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test(expected = NotEnoughStockException.class)
    public void 재고수량초과3() throws Exception {
        //given
        final int movie_count = 16;

        Member member = createMember();
        Product movie = createMovie(movie_name, movie_price, movie_quantity);

        //when
        orderService.order(member.getId(), movie.getId(), movie_count);

        //then
        fail("영화의 재고 수량 부족 예외가 발생해야 한다.");
    }
    
}