package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.book.links.BookPopularityEntity;
import com.example.MyBookShopApp.struct.book.links.BookRatingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookPopularityRepository extends JpaRepository<BookPopularityEntity,Integer> {


    @Query(value = "select  id, book_id, cart, postponed, bought, (0.7*cart+0.4*postponed+bought) as popularity from book_popularity", nativeQuery = true )
    Page<BookPopularityEntity> findAllByOrderByPopularity(Pageable nextPage);

    @Query(value = "select (0.7*cart+0.4*postponed+bought) as popularity  from book_popularity where book_id=?1", nativeQuery = true )
    Integer getPopularityByBookId(Integer bookId);


}
