package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.book.links.BookRatingEntity;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRatingEntity, Integer> {
    //  Page<BookRatingEntity> findAllByOrderByPopularity(Pageable nextPage);
    Page<BookRatingEntity> findAllByOrderByRating(Pageable nextPage);

    List<BookRatingEntity> findAllByBookId(Integer bookId);

    @Query(value = "select rating, COUNT(*) from book_rating where book_id=?1 GROUP BY rating ORDER BY rating DESC", nativeQuery = true)
    List<Object[]> countRatingByBookId(Integer bookId);

    @Query(value = "select avg(rating) from book_rating where book_id=?1", nativeQuery = true)
    Integer findRatingByBookId(Integer bookId);
}
