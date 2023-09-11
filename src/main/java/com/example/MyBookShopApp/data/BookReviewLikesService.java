package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.BookReviewLikesRepository;
import com.example.MyBookShopApp.struct.book.review.BookReviewLikeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookReviewLikesService {
    private BookReviewLikesRepository bookReviewLikesRepository;

    @Autowired
    public BookReviewLikesService(BookReviewLikesRepository bookReviewLikesRepository) {
        this.bookReviewLikesRepository = bookReviewLikesRepository;
    }

    public List<BookReviewLikeEntity> findAllByReviewId(Integer reviewId) {
        return bookReviewLikesRepository.findAllByReviewId(reviewId);
    }

    public void save(BookReviewLikeEntity bookReviewLikeEntity) {
        bookReviewLikesRepository.save(bookReviewLikeEntity);
    }

    public void delete(BookReviewLikeEntity bookReviewLikeEntity) {
        bookReviewLikesRepository.delete(bookReviewLikeEntity);
    }

    public List<BookReviewLikeEntity> getByReviewIdByUserId(Integer reviewId, Integer userId) {
        return bookReviewLikesRepository.findByReviewIdByUserId(reviewId, userId);
    }


}
