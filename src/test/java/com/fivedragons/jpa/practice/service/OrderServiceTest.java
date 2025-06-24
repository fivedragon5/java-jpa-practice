package com.fivedragons.jpa.practice.service;

import com.fivedragons.jpa.practice.domain.Address;
import com.fivedragons.jpa.practice.domain.Member;
import com.fivedragons.jpa.practice.domain.Order;
import com.fivedragons.jpa.practice.domain.OrderStatus;
import com.fivedragons.jpa.practice.domain.item.Book;
import com.fivedragons.jpa.practice.exception.NotEnoughStockException;
import com.fivedragons.jpa.practice.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    
    @Test
    void 상품_주문() {
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(orderId, getOrder.getId());
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    void 상품_주문_재고수량_초과() {
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);
        int orderCount = 11;

        // when & then
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        });
    }

    @Test
    void 주문_취소() {
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);
        
        // then
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals(findOrder.getStatus(), OrderStatus.CANCEL, "주문 취소시 주문의 상태가 CANCEL 상태가 된다.");
        assertEquals(10, book.getStockQuantity(), "주문 취소시 재고도 다시 원복된다.");
    }

    private Book createBook(String name, int orderPrice, int orderCount) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(orderPrice);
        book.setStockQuantity(orderCount);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("fad");
        member.setAddress(new Address("서울", "1234", "1234-1234"));
        em.persist(member);
        return member;
    }
}