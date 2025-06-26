package com.fivedragons.jpa.practice.api;

import com.fivedragons.jpa.practice.domain.Address;
import com.fivedragons.jpa.practice.domain.Order;
import com.fivedragons.jpa.practice.domain.OrderSearch;
import com.fivedragons.jpa.practice.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderService.findOrders(new OrderSearch());
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private String orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName(); // Lazy 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus().name();
            this.address = order.getDelivery().getAddress(); // Lazy 초기화
        }
    }
}
