package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Tag2BookService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.data.dto.TagListDto;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.struct.book.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class MainPageController {

    private final BookService bookService;
    private final Tag2BookService tag2BookService;

    @Autowired
    public MainPageController(BookService bookService, Tag2BookService tag2BookService) {
        this.bookService = bookService;
        this.tag2BookService = tag2BookService;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> recommendedBooks() {
        return bookService.getPageOfRecommendedBook(1, 20);
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> recentBooks() {
        return bookService.getPageOfRecentBooks(1, 20).getContent();
    }

    @ModelAttribute("popularBooks")
    public List<BookEntity> popularBooks() {
        return bookService.getPageOfPopularBooks(1, 20);
    }


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResults")
    public List<BookEntity> searchResults() {
        return new ArrayList<>();
    }


    @GetMapping("/")
    public String mainPage(Model model) {
        List<TagListDto> tagListDto = tag2BookService.getTagListDto();

        model.addAttribute("tagListDto", tagListDto);
        return "index";
    }

    @GetMapping("books/recommended")
    @ResponseBody
    public BooksPageDto pagedRecommendedBooks(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBookNoAuthor(offset, limit).getContent());
    }

    @GetMapping(value = {"search/", "search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        Logger.getLogger(MainPageController.class.getName()).info("wwww");
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults", bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 20).getContent());

            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск не возможен по null");
        }


    }

}
