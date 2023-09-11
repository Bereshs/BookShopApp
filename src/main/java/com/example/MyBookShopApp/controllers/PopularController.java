package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class PopularController {
    BookService bookService;

    @Autowired
    public PopularController(BookService bookService) {
        this.bookService = bookService;
    }


    @ModelAttribute("popularBooksList")
    public List<BookEntity> popularBooks(){
        return bookService.getPageOfPopularBooks(0,20);
    }

    @GetMapping("/books/popular")
    public String popularPage() {
        return "/books/popular";
    }

}
