package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class RecentController {
    BookService bookService;

    public RecentController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recentBooksList")
    public List<BookEntity> recentBooks() {
        return bookService.getPageOfRecentBooks(0, 20).getContent();
    }

    @GetMapping("/books/recent")
    public String recentPage(Model model) {
        return "/books/recent";
    }



}
