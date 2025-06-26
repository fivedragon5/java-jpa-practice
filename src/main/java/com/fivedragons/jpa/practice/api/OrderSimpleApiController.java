package com.fivedragons.jpa.practice.api;

import com.fivedragons.jpa.practice.domain.Order;
import com.fivedragons.jpa.practice.domain.OrderSearch;
import com.fivedragons.jpa.practice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne(ManyToOne, OneToOne) 관계에서의 성능 최적화
 * Order
 * Order -> Member
 * Order -> Deliver
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderService orderService;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        // Order -> Member -> Order -> Member -> ... 무한 루프
        List<Order> orders = orderService.findOrders(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return orders;
    }
}
