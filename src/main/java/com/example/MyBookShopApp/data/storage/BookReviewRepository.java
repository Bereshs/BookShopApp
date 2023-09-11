package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.book.review.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Integer> {
    List<BookReviewEntity> findAllByBookId(Integer bookId);
    @Query(value = "select * from book_review where book_id=?1 and user_id=?2", nativeQuery = true)
    List<BookReviewEntity> findAllByBookIdAndUserId(Integer bookId, Integer userId);
}
