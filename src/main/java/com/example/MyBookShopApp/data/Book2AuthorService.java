package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.Book2AuthorRepository;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.book.links.Book2AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Book2AuthorService {
    private Book2AuthorRepository book2AuthorRepository;

    public Book2AuthorService(Book2AuthorRepository book2AuthorRepository) {
        this.book2AuthorRepository = book2AuthorRepository;
    }

    public List<Book2AuthorEntity> getBooks2Author() {
        return book2AuthorRepository.findAll();
    }

    public Page<Book2AuthorEntity> getPageOfBooksByAuthorId(Integer id, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return book2AuthorRepository.findPageOfBooksByAuthorId(id, nextPage);
    }

    public List<BookEntity> getBookEntity(Page<Book2AuthorEntity> book2AuthorEntities) {
        List<BookEntity> bookEntities = new ArrayList<>();
        for (Book2AuthorEntity book2Author : book2AuthorEntities) {
            bookEntities.add(book2Author.getBook());
        }
        return bookEntities;
    }


    public Integer getCountBooksByAuthorId(Integer id) {
        return book2AuthorRepository.findBooksByAuthorId(id).size();
    }
}
