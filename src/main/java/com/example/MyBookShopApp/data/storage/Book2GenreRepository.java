package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.book.links.Book2GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Book2GenreRepository extends JpaRepository<Book2GenreEntity, Integer> {
    Integer countByGenreId(int genreId);

    Page<Book2GenreEntity> findAllByGenreId(int genreId, Pageable nextPage);

    List<Book2GenreEntity> findAllByBookId(int bookId);
}
