package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.PaymentService;
import com.example.MyBookShopApp.data.dto.PaymentDto;
import com.example.MyBookShopApp.security.BookstoreUserRegister;
import com.example.MyBookShopApp.struct.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {
    private final BookstoreUserRegister userRegister;
    private final PaymentService paymentService;
    private final BookService bookService;


    @Autowired
    public ProfileController(BookstoreUserRegister userRegister, PaymentService paymentService, BookService bookService) {
        this.userRegister = userRegister;
        this.paymentService = paymentService;
        this.bookService = bookService;
    }


    @GetMapping("/profile")
    public String handleProfile(Model model) {
        UserEntity user = userRegister.getCurrentUser();
        List<BalanceTransactionEntity> transactionEntityList = paymentService.getListByUserId(user.getId());
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        transactionEntityList.forEach(transaction -> {
            paymentDtoList.add(paymentDtoFromTransaction(transaction));
        });
        model.addAttribute("paymentDtoList", paymentDtoList);
        return "profile";
    }

    public PaymentDto paymentDtoFromTransaction(BalanceTransactionEntity transaction) {
        String slug = transaction.getBookId() > 0 ? bookService.getById(transaction.getBookId()).getSlug() : "";
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setSum(transaction.getValue());
        paymentDto.setDescription(transaction.getDescription());
        paymentDto.setSlug(slug);
        paymentDto.setTime(transaction.getTime());
        return paymentDto;
    }
}
