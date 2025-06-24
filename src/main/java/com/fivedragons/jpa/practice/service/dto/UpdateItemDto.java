package com.fivedragons.jpa.practice.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateItemDto {
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
