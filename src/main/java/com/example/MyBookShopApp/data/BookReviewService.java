package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.BookReviewRepository;
import com.example.MyBookShopApp.struct.book.review.BookReviewEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookReviewService {

    BookReviewRepository bookReviewRepository;

    @Autowired
    public BookReviewService(BookReviewRepository bookReviewRepository) {
        this.bookReviewRepository = bookReviewRepository;
    }

    public void save(BookReviewEntity bookReviewEntity) {
        bookReviewRepository.save(bookReviewEntity);
    }

    public List<BookReviewEntity> getAllByBookIdAndUserId(Integer bookId, Integer userId) {
        return bookReviewRepository.findAllByBookIdAndUserId(bookId, userId);
    }

    public List<BookReviewEntity> findAllByBookId(Integer bookId) {
        return bookReviewRepository.findAllByBookId(bookId);
    }
}
