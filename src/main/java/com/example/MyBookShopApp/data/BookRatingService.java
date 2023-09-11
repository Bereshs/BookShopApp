package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.BookRatingRepository;
import com.example.MyBookShopApp.struct.book.links.BookRatingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookRatingService {
    BookRatingRepository bookRatingRepository;

    @Autowired
    public BookRatingService(BookRatingRepository bookRatingRepository) {
        this.bookRatingRepository = bookRatingRepository;
    }

    public List<BookRatingEntity> getAllByBookId(Integer bookId) {
        return bookRatingRepository.findAllByBookId(bookId);
    }

    public void save(BookRatingEntity bookRating) {
        bookRatingRepository.save(bookRating);
    }

    public List<Integer> getBookStars(Integer bookId) {
        List<Object[]> starsMap = bookRatingRepository.countRatingByBookId(bookId);
        Integer[] stars = new Integer[5];

        for (Object[] obj : starsMap) {
            int pointer = 5 - Integer.parseInt(obj[0].toString());
            stars[pointer] = Integer.parseInt(obj[1].toString());
        }

        return Arrays.asList(stars);
    }

    public Integer getRatingByBookId(Integer bookId) {
        return bookRatingRepository.findRatingByBookId(bookId);
    }
}
