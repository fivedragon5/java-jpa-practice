package com.fivedragons.jpa.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaPracticeApplication {

	public static void main(String[] args) {
		Hello hello = new Hello();
		hello.setData("Hello, World!");
		System.out.println(hello.getData());
		SpringApplication.run(JpaPracticeApplication.class, args);
	}
}
