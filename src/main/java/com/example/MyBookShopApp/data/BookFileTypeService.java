package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.BookFileTypeRepository;
import com.example.MyBookShopApp.struct.book.file.BookFileTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookFileTypeService {

    private  final BookFileTypeRepository bookFileTypeRepository;

    @Autowired
    public BookFileTypeService(BookFileTypeRepository bookFileTypeRepository) {
        this.bookFileTypeRepository = bookFileTypeRepository;
    }


    public List<BookFileTypeEntity> getAll() {
        return bookFileTypeRepository.findAll();
    }
}
