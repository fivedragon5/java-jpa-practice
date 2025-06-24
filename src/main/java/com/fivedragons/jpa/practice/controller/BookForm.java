package com.fivedragons.jpa.practice.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class BookForm {

    private Long id;

    @NotEmpty(message = "상품 이름은 필수 입니다.")
    private String name;
    @Range(min = 0, max = Integer.MAX_VALUE, message = "상품 가격을 올바르게 입력해 주세요.")
    private int price;
    @Range(min = 0, max = Integer.MAX_VALUE, message = "상품 수량을 올바르게 입력해 주세요.")
    private int stockQuantity;

    private String author;
    private String isbn;
}
