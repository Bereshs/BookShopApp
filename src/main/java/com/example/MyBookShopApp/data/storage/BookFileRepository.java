package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.book.file.BookFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookFileRepository extends JpaRepository<BookFileEntity, Integer> {
    public BookFileEntity findBookByHash(String hash);

    public List<BookFileEntity> findFilesByBookId(Integer bookId);

}
