package com.fivedragons.jpa.practice.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("B")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Book extends Item {
    private String author;
    private String isbn;
}
