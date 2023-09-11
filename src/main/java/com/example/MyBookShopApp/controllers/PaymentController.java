package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.PaymentService;
import com.example.MyBookShopApp.data.dto.PaymentDataTransfer;
import com.example.MyBookShopApp.security.BookstoreUserRepository;
import com.example.MyBookShopApp.security.BookstoreUserService;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
public class PaymentController {


    private final JWTUtil jwtUtil;
    private final PaymentService paymentService;
    private final BookService bookService;
    private final BookstoreUserService bookstoreUserService;


    @Autowired
    public PaymentController(JWTUtil jwtUtil, PaymentService paymentService, BookService bookService, BookstoreUserService bookstoreUserService) {
        this.jwtUtil = jwtUtil;
        this.paymentService = paymentService;
        this.bookService = bookService;
        this.bookstoreUserService = bookstoreUserService;
    }

    @GetMapping("/payment")
    public String paymentHandleController() {


        return "redirect:/profile";
    }

    @PostMapping("/payment")
    public String paymentPostHandleController(@RequestBody PaymentDataTransfer payment, @CookieValue String token) throws URISyntaxException, NoSuchAlgorithmException {
        UserEntity user = bookstoreUserService.findUserByEmail(jwtUtil.extractUsername(token));
        if (user != null && payment.getSum() != null && payment.getSum() > 0) {
   //         paymentService.createFakeRequestTransaction(payment.getSum(), user, "Пополнение счета из ЛК");
            paymentService.createBankTransaction(payment.getSum(), user, "Пополнение счета из ЛК");
        }

        return "redirect:/payment";
    }

    @GetMapping("/books/pay")
    public RedirectView handlePay(@CookieValue(value = "cartContents", required = false) String cartContents, @CookieValue String token) throws NoSuchAlgorithmException, URISyntaxException {
        List<BookEntity> booksFromCookieSlugs = bookService.getBooksBySlugIn(paymentService.getCartContents(cartContents));
        double paymentSumTotal = booksFromCookieSlugs.stream().mapToDouble(BookEntity::getPrice).sum();
        UserEntity user = bookstoreUserService.findUserByEmail(jwtUtil.extractUsername(token));

        if (paymentSumTotal > user.getBalance()) {
            int needAmountMoney = (int) (paymentSumTotal - user.getBalance());
            return  paymentService.createBankTransaction(needAmountMoney, user, cartContents);
        }
        paymentService.createTransactionsFromBookList(booksFromCookieSlugs, user);
        return new RedirectView("http://localhost:8085/payment");
    }

}
