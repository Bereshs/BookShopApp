package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookRatingService;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.PaymentService;
import com.example.MyBookShopApp.data.dto.BookDto;
import com.example.MyBookShopApp.data.storage.BookRepository;
import com.example.MyBookShopApp.struct.book.BookEntity;
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
public class CartController {
    private final BookService bookService;
    private final BookRatingService bookRatingService;



    @Autowired
    public CartController(BookService bookService, BookRatingService bookRatingService) {
        this.bookService = bookService;
        this.bookRatingService = bookRatingService;
    }

    @ModelAttribute(name = "bookCart")
    public List<BookEntity> bookCart() {
        return new ArrayList<>();
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents, Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);

        } else {
            model.addAttribute("isCartEmpty", false);
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
            Integer totalPrice = bookDtoList.stream().map(BookDto::getBook).mapToInt(BookEntity::getPrice).sum();
            Integer totalFullPrice = bookDtoList.stream().map(BookDto::getBook).mapToInt(BookEntity::discountPrice).sum();
            model.addAttribute("bookDtoList", bookDtoList);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalFullPrice", totalFullPrice);
        }

        return "cart";
    }

    @PostMapping("/books/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug,
                                                  @CookieValue(name = "cartContents", required = false) String cartContents,
                                                  HttpServletResponse response,
                                                  Model model) {

        if (cartContents != null && !cartContents.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", false);
        }

        return "redirect:/books/cart";
    }


    @PostMapping("/books/changeBookStatus/cart/{slug}")
    public String handleChangeBookStatus(@PathVariable(value = "slug") String slug, @CookieValue(name = "cartContents", required = false
    ) String cartContents, HttpServletResponse response, Model model) {
        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie("cartContents", slug);
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        }

        return "redirect:/books/" + slug;
    }


 }
