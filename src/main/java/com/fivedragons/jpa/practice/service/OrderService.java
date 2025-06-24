package com.fivedragons.jpa.practice.service;

import com.fivedragons.jpa.practice.domain.Delivery;
import com.fivedragons.jpa.practice.domain.Member;
import com.fivedragons.jpa.practice.domain.Order;
import com.fivedragons.jpa.practice.domain.OrderItem;
import com.fivedragons.jpa.practice.domain.OrderSearch;
import com.fivedragons.jpa.practice.domain.item.Item;
import com.fivedragons.jpa.practice.repository.ItemRepository;
import com.fivedragons.jpa.practice.repository.MemberRepository;
import com.fivedragons.jpa.practice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 회원 조회
        Member member = memberRepository.findOne(memberId);
        // 아이템 조회
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createORder(member, delivery, orderItem);

        // 주문 저장 : delivery, orderItem Cascade 로 한번에 저장
        orderRepository.save(order);

        // 식별자 값 반환
        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrder(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
