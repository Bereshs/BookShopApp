package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookRatingService;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.dto.BookDto;
import com.example.MyBookShopApp.struct.book.BookEntity;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
public class PostponedController {

    private final BookService bookService;
    private final BookRatingService bookRatingService;

    @Autowired
    public PostponedController(BookService bookService, BookRatingService bookRatingService) {
        this.bookService = bookService;
        this.bookRatingService = bookRatingService;
    }



    @PostMapping("/books/changeBookStatus/kept/{slug}")
    public String handleChangeBookStatusKept(@PathVariable(value = "slug") String slug, @CookieValue(name = "keptContents", required = false
    ) String cartContents, HttpServletResponse response, Model model) {
        Logger.getLogger(PostponedController.class.getName()).info("changestatus cookies " + cartContents);

        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie("keptContents", slug);
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isKeptEmpty", false);
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("keptContents", stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isKeptEmpty", false);
        }
        return "redirect:/books/" + slug;
    }

    @GetMapping({"/postponed", "/books/postponed"})
    public String handlePostponedRequest(@CookieValue(value = "keptContents", required = false) String cartContents, Model model) {
        Logger.getLogger(PostponedController.class.getName()).info("postponed cookies " + cartContents);

        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isKeptEmpty", true);
        } else {
            model.addAttribute("isKeptEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookiesSlug = cartContents.split("/");
            List<BookEntity> bookFromCookiesSlugs = bookService.getBooksBySlugIn(cookiesSlug);
            List<BookDto> bookDtoList = new ArrayList<>();
            bookFromCookiesSlugs.forEach(book->{
                BookDto bookDto = new BookDto();
                bookDto.setBook(book);
                bookDto.setRating(bookRatingService.getRatingByBookId(book.getId()));
                bookDtoList.add(bookDto);
            });

            Integer totalPrice = bookDtoList.stream().map(BookDto::getBook).mapToInt(BookEntity::getPrice).sum(); //bookFromCookiesSlugs.stream().mapToInt(BookEntity::getPrice).sum();
            Integer totalFullPrice = bookDtoList.stream().map(BookDto::getBook).mapToInt(BookEntity::discountPrice).sum(); //bookFromCookiesSlugs.stream().mapToInt(BookEntity::discountPrice).sum();
            model.addAttribute("bookDtoList", bookDtoList);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalFullPrice", totalFullPrice);
            model.addAttribute("listSlugs",cartContents.replace("/", ", ") );
        }
        return "postponed";
    }

    @PostMapping("/books/changeBookStatus/kept/remove/{slug}")
    public String handleRemoveBookFromKeptRequest(@PathVariable("slug") String slug,
                                                  @CookieValue(name = "keptContents", required = false) String cartContents,
                                                  HttpServletResponse response,
                                                  Model model) {
        if (cartContents != null && !cartContents.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("keptContents", String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isKeptEmpty", false);
        } else {
            model.addAttribute("isKeptEmpty", false);
        }
        return "redirect:/postponed";
    }

}
