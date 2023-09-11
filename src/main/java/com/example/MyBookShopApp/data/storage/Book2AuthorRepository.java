package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.book.links.Book2AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Book2AuthorRepository extends JpaRepository<Book2AuthorEntity, Integer> {
    Page<Book2AuthorEntity> findPageOfBooksByAuthorId(Integer authorId, Pageable nextPage);

    List<Book2AuthorEntity> findBooksByAuthorId(Integer authorId);

}
