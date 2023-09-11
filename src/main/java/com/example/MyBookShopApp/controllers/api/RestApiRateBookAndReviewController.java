package com.example.MyBookShopApp.controllers.api;

import com.example.MyBookShopApp.data.*;
import com.example.MyBookShopApp.data.dto.ApiSimpleResponse;
import com.example.MyBookShopApp.data.dto.RateBookReviewTransfer;
import com.example.MyBookShopApp.data.dto.RateBookTransfer;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.book.links.BookRatingEntity;
import com.example.MyBookShopApp.struct.book.review.BookReviewEntity;
import com.example.MyBookShopApp.struct.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.struct.user.UserEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api")
@Api("Api for change rating books and reviews")
public class RestApiRateBookAndReviewController {

    BookRatingService bookRatingService;
    BookService bookService;
    BookReviewLikesService bookReviewLikesService;
    BookReviewService bookReviewService;

    UserService userService;

    @Autowired
    public RestApiRateBookAndReviewController(BookRatingService bookRatingService, BookService bookService, BookReviewLikesService bookReviewLikesService, BookReviewService bookReviewService, UserService userService) {
        this.bookRatingService = bookRatingService;
        this.bookService = bookService;
        this.bookReviewLikesService = bookReviewLikesService;
        this.bookReviewService = bookReviewService;
        this.userService = userService;
    }


    @PostMapping("/rateBookReview")
    public ApiSimpleResponse setRateBookReview(@RequestBody RateBookReviewTransfer rateBookReviewTransfer) {
        BookReviewLikeEntity bookReviewLikeEntity;
        List<BookReviewLikeEntity> bookReviewLikeEntityList = bookReviewLikesService.getByReviewIdByUserId(rateBookReviewTransfer.getReviewId(), getAuthenticateUserId());
        if (bookReviewLikeEntityList.size() == 0) {
            bookReviewLikeEntity = new BookReviewLikeEntity();
            bookReviewLikeEntity.setUserId(getAuthenticateUserId());
            bookReviewLikeEntity.setReviewId(rateBookReviewTransfer.getReviewId());
        } else {
            bookReviewLikeEntity = bookReviewLikeEntityList.get(0);
        }
        bookReviewLikeEntity.setValue(rateBookReviewTransfer.getValue());
        bookReviewLikeEntity.setTime(LocalDateTime.now());
        bookReviewLikesService.save(bookReviewLikeEntity);
        return new ApiSimpleResponse(true);
    }

    @PostMapping("/rateBook")
    public ApiSimpleResponse setRateBook(@RequestBody RateBookTransfer rateBookTransfer) {
        BookRatingEntity ratingEntity = new BookRatingEntity();
        BookEntity book = bookService.getBookById(rateBookTransfer.getBookId());
        if (book == null) {
            new ApiSimpleResponse(false);
        }
        ratingEntity.setBook(book);
        ratingEntity.setRating(rateBookTransfer.getBookId());
        ratingEntity.setUserId(getAuthenticateUserId());
        bookRatingService.save(ratingEntity);
        return new ApiSimpleResponse(true);
    }

    @PostMapping("/bookReview")
    public ApiSimpleResponse setBookReview(@RequestParam("bookId") Integer bookId, @RequestParam("text") String text) {
        List<BookReviewEntity> reviewEntityList = bookReviewService.getAllByBookIdAndUserId(bookId, getAuthenticateUserId());//new BookReviewEntity();
        BookReviewEntity reviewEntity;
        if (text.length() < 10) {
            return new ApiSimpleResponse(false);
        }
        if (reviewEntityList.size() == 0) {
            reviewEntity = new BookReviewEntity();
            reviewEntity.setBookId(bookId);
            reviewEntity.setUser(userService.getById(getAuthenticateUserId()));
            Logger.getLogger("sss").info("Creating new entity");
        } else {
            reviewEntity = reviewEntityList.get(0);
        }

        reviewEntity.setTime(LocalDateTime.now());
        reviewEntity.setText(text);
        bookReviewService.save(reviewEntity);
        return new ApiSimpleResponse(true);
    }

    public Integer getAuthenticateUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.getByEmail(authentication.getName());
        if (user != null) {
            return user.getId();
        }
        return 0;
    }

}

