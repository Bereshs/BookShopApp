package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.dto.AuthorDto;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.data.Book2AuthorService;
import com.example.MyBookShopApp.struct.author.AuthorEntity;
import com.example.MyBookShopApp.data.AuthorService;
import com.example.MyBookShopApp.struct.book.BookEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Api(description = "authors data")
public class AuthorsController {

    private final AuthorService authorService;
    private final Book2AuthorService book2AuthorService;


    @Autowired
    public AuthorsController(AuthorService authorService, Book2AuthorService book2AuthorService) {
        this.authorService = authorService;
        this.book2AuthorService = book2AuthorService;
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<AuthorEntity>> authorsMap() {
        return authorService.getAuthorsMap();
    }

    @GetMapping("/authors")
    public String authorsPage() {
        return "/authors/index";
    }

    @GetMapping("/authors/{id}")
    public String authorPage(@PathVariable(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("authorDto", getAuthorDto(authorService.getAuthor(id)));
        model.addAttribute("booksList", book2AuthorService.getBookEntity(
                book2AuthorService.getPageOfBooksByAuthorId(id, 0, 5)));
        model.addAttribute("allBooksList", new ArrayList<BookEntity>());
        model.addAttribute("countBooks", book2AuthorService.getCountBooksByAuthorId(id));
        model.addAttribute("refreshid", id);

        return "/authors/slug";
    }


    @GetMapping("/api/authors")
    @ApiOperation("method to det authors Map")
    @ResponseBody
    public Map<String, List<AuthorEntity>> authors() {
        return authorService.getAuthorsMap();
    }

    public AuthorDto getAuthorDto(AuthorEntity author) {
        AuthorDto result = new AuthorDto();
        result.setName(author.getName());
        result.setDescription(author.getDescription());
        result.setPhoto(author.getPhoto());
        return result;
    }
}

