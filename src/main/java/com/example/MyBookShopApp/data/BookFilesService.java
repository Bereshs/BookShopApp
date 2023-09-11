package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.BookFileRepository;
import com.example.MyBookShopApp.struct.book.file.BookFileEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookFilesService {
    BookFileRepository bookFileRepository;

    public BookFilesService(BookFileRepository bookFileRepository) {
        this.bookFileRepository = bookFileRepository;
    }
    public List<BookFileEntity> getFilesByBookId(Integer bookId) {
        return bookFileRepository.findFilesByBookId(bookId);
    }
}
