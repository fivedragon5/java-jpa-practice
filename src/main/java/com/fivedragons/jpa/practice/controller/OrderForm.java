package com.fivedragons.jpa.practice.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class OrderForm {

    @NotEmpty(message = "회원을 선택해 주세요.")
    private Long memberId;
    @NotEmpty(message = "상품을 선택해 주세요")
    private Long itemId;

    @Range(min = 0, max = Integer.MAX_VALUE, message = "상품 수량을 올바르게 입력해 주세요.")
    private int stockQuantity;
}
