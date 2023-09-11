package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.book.review.BookReviewEntity;
import com.example.MyBookShopApp.struct.book.review.BookReviewLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewLikesRepository extends JpaRepository<BookReviewLikeEntity, Integer> {
public List<BookReviewLikeEntity> findAllByReviewId(Integer reviewId);


@Query(value = "select * from book_review_like where review_id=?1 and user_id=?2", nativeQuery = true)
public List<BookReviewLikeEntity> findByReviewIdByUserId(Integer reviewId, Integer userId);
}
