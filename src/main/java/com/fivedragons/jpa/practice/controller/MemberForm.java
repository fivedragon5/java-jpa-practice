package com.fivedragons.jpa.practice.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름을 필수 입니다")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
