package com.fivedragons.jpa.practice.api;

import com.fivedragons.jpa.practice.domain.Address;
import com.fivedragons.jpa.practice.domain.Order;
import com.fivedragons.jpa.practice.domain.OrderItem;
import com.fivedragons.jpa.practice.domain.OrderSearch;
import com.fivedragons.jpa.practice.domain.OrderStatus;
import com.fivedragons.jpa.practice.repository.OrderRepository;
import com.fivedragons.jpa.practice.repository.order.query.OrderFlatDto;
import com.fivedragons.jpa.practice.repository.order.query.OrderItemQueryDto;
import com.fivedragons.jpa.practice.repository.order.query.OrderQueryDto;
import com.fivedragons.jpa.practice.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToMany()
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        for (Order o : orders) {
            System.out.println("order ref = " + o + "id = " + o.getId());
        }
        return orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        return orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDtoOptimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> allByDtoFlat = orderQueryRepository.findAllByDtoFlat();
        // OrderFlatDto를 OrderQueryDto로 변환 orderId로 묶기
        return allByDtoFlat.stream()
                .collect(Collectors.groupingBy(OrderFlatDto::getOrderId))
                .values().stream()
                .map(orderFlatDtos -> {
                    OrderFlatDto first = orderFlatDtos.get(0);
                    OrderQueryDto orderQueryDto = new OrderQueryDto(
                            first.getOrderId(),
                            first.getName(),
                            first.getOrderDate(),
                            first.getOrderStatus(),
                            first.getAddress()
                    );
                    List<OrderItemQueryDto> orderItems = orderFlatDtos.stream()
                            .map(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()))
                            .collect(Collectors.toList());
                    orderQueryDto.setOrderItems(orderItems);
                    return orderQueryDto;
                })
                .collect(Collectors.toList());
    }

    @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
                            .map(OrderItemDto::new)
                            .collect(Collectors.toList());
            order.getOrderItems().stream().forEach(o -> o.getItem().getName());
        }
    }

    @Getter
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
