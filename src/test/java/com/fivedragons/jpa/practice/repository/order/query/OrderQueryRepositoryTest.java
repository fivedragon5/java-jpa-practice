package com.fivedragons.jpa.practice.repository.order.query;

import com.fivedragons.jpa.practice.domain.Delivery;
import com.fivedragons.jpa.practice.domain.Member;
import com.fivedragons.jpa.practice.domain.Order;
import com.fivedragons.jpa.practice.domain.OrderItem;
import com.fivedragons.jpa.practice.domain.item.Book;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(OrderQueryRepository.class)
@DataJpaTest
class OrderQueryRepositoryTest {

    @Autowired
    private OrderQueryRepository orderQueryRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("회원의 주문을 조회하면 주문 정보와 주문 아이템 정보를 함께 조회한다.")
    void findOrderQueryDtos_Test() {
        // given
        String userName = "fad";
        String bookName1 = "fad test book1";
        String bookName2 = "fad test book2";
        Member member = createMember("fad");
        Book book1 = createBook(bookName1, 10000, 100);
        Book book2 = createBook(bookName2, 20000, 200);
        OrderItem orderItem1 = createOrderItem(book1, 10000, 1);
        OrderItem orderItem2 = createOrderItem(book2, 20000, 2);
        Delivery delivery = createDelivery(member);
        Order order = createOrder(member, delivery, orderItem1, orderItem2);

        // when
        List<OrderQueryDto> result = orderQueryRepository.findOrderQueryDtos();
        
        // then
        Assertions.assertEquals(result.size(), 1);
        OrderQueryDto dto = result.get(0);
        Assertions.assertEquals(dto.getName(), userName);
        // book에 대한 테스트 코드
        Assertions.assertEquals(dto.getOrderItems().size(), 2);
    }
    
    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Delivery createDelivery(Member member) {
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        em.persist(delivery);
        return delivery;
    }

    private OrderItem createOrderItem(Book book, int orderPrice, int count) {
        OrderItem orderItem = OrderItem.createOrderItem(book, orderPrice, count);
        em.persist(orderItem);
        return orderItem;
    }

    private Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = Order.createOrder(member, delivery, orderItems);
        em.persist(order);
        return order;
    }
}