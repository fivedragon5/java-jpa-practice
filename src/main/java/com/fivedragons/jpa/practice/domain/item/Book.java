package com.fivedragons.jpa.practice.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book extends Item {
    private String author;
    private String isbn;
}
