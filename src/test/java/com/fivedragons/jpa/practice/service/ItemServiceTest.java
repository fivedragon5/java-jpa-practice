package com.fivedragons.jpa.practice.service;

import com.fivedragons.jpa.practice.domain.item.Book;
import com.fivedragons.jpa.practice.domain.item.Item;
import com.fivedragons.jpa.practice.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class ItemServiceTest {

    @Autowired private ItemService itemService;
    @Autowired private ItemRepository itemRepository;

    @Test
    void 상품_저장() {
        // given
        Book book = Book.builder()
                .author("fad")
                .isbn("B612")
                .name("테스트")
                .price(10000)
                .stockQuantity(10)
                .build();

        // when
        itemService.saveItem(book);

        // then
        Item findItem = itemRepository.findOne(book.getId());
        Assertions.assertEquals(book, findItem);
    }
}