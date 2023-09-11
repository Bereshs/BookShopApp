package com.example.MyBookShopApp.controllers.api;

import com.example.MyBookShopApp.data.PaymentService;
import com.example.MyBookShopApp.data.dto.ApiSimpleResponse;
import com.example.MyBookShopApp.data.storage.BookRepository;
import com.example.MyBookShopApp.data.storage.UserRepository;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.struct.payments.TransactionStatus;
import com.example.MyBookShopApp.struct.user.UserEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping("/api")
@Api("Payment Api controller")
public class RestApiPaymentController {

    public final PaymentService paymentService;
    private final UserRepository userRepository;

    private final BookRepository bookRepository;


    @Autowired
    public RestApiPaymentController(PaymentService paymentService, UserRepository userRepository, BookRepository bookRepository) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }


    @PostMapping("/payment")
    public ApiSimpleResponse postPaymentHandle(@RequestParam String hash, @RequestParam Integer sum, @RequestParam Long time) {
        BalanceTransactionEntity transaction = paymentService.getByHash(hash);
        if (transaction.getStatus().equals(TransactionStatus.created)) {
            String description = transaction.getDescription();
            paymentService.confirmTransaction(hash, "Пополнение счета");
            UserEntity user = userRepository.getById(transaction.getUserId());
            String[] cartContents = paymentService.getCartContents(description);
            List<BookEntity> bookList = bookRepository.findBooksBySlugIn(cartContents);
            paymentService.createTransactionsFromBookList(bookList, user);
        }

        return new ApiSimpleResponse(true);
    }

}
