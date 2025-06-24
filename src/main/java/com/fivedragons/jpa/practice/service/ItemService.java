package com.fivedragons.jpa.practice.service;

import com.fivedragons.jpa.practice.domain.item.Book;
import com.fivedragons.jpa.practice.domain.item.Item;
import com.fivedragons.jpa.practice.repository.ItemRepository;
import com.fivedragons.jpa.practice.service.dto.UpdateItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long itemId, UpdateItemDto dto) {
        Book findItem = (Book) itemRepository.findOne(itemId);
        findItem.setName(dto.getName());
        findItem.setPrice(dto.getPrice());
        findItem.setStockQuantity(dto.getStockQuantity());
        findItem.setAuthor(dto.getAuthor());
        findItem.setIsbn(dto.getIsbn());
        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item fineOne(Long id) {
        return itemRepository.findOne(id);
    }
}
