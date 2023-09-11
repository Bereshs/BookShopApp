package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.tags.Tag2BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface Tag2BookRepository extends JpaRepository<Tag2BookEntity, Integer> {
    Integer countByTagId(int bookId);

    Page<Tag2BookEntity> getBooksByTagId(Integer tagId, Pageable nextPage);

    List<Tag2BookEntity> findTagsByBookId(Integer bookId);
}
