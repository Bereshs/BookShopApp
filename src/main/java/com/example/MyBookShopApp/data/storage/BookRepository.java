package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.book.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDateTime;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    //   List<BookEntity> findBooksByAuthorFirstNameContaining(String authorFirstName);

    List<BookEntity> findBooksByTitleContaining(String bookTitle);

    List<BookEntity> findBooksByPriceBetween(Integer min, Integer max);

    BookEntity findBookBySlug(String slug);


    List<BookEntity> findBooksByPriceIs(Integer price);

    @Query(value = "SELECT * FROM book WHERE is_bestseller=1", nativeQuery = true)
    List<BookEntity> getBestsellers();

    @Query(value = "SELECT * FROM book WHERE discount=(SELECT MAX(discount) FROM BOOK)", nativeQuery = true)
    List<BookEntity> getBooksWithMaxDiscount();

    Page<BookEntity> findBookByTitleContaining(String bootTitle, Pageable nextPage);

    Page<BookEntity> findAllByOrderByPubDate(Pageable nextPage);

    Page<BookEntity> findAllByOrderByTitle(Pageable nextPage);

    Page<BookEntity> findBooksByPubDateBetween(LocalDateTime min, LocalDateTime max, Pageable nextPage);

    @Query(value = "SELECT * FROM book WHERE is_bestseller=1", nativeQuery = true)
    Page<BookEntity> findAllBestsellers(Pageable nextPage);

    List<BookEntity> findBooksBySlugIn(String[] slug);
}
