package com.fivedragons.jpa.practice.controller;

import com.fivedragons.jpa.practice.domain.item.Book;
import com.fivedragons.jpa.practice.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@Valid BookForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "items/createItemForm";
        }
        Book book = Book.createBook(
                form.getName(),
                form.getPrice(),
                form.getStockQuantity(),
                form.getAuthor(),
                form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/";
    }

}
