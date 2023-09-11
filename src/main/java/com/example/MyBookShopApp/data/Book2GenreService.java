package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.Book2GenreRepository;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.book.links.Book2GenreEntity;
import com.example.MyBookShopApp.struct.genre.GenreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Book2GenreService {
    Book2GenreRepository book2GenreRepository;

    @Autowired
    public Book2GenreService(Book2GenreRepository book2GenreRepository) {
        this.book2GenreRepository = book2GenreRepository;
    }

    public Integer getCountBooks(Integer genreId) {
        return book2GenreRepository.countByGenreId(genreId);
    }

    public List<Book2GenreEntity> getAll() {
        return book2GenreRepository.findAll();
    }

    public Page<Book2GenreEntity> getAllByGenreId(Integer genreId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return book2GenreRepository.findAllByGenreId(genreId, nextPage);
    }

    public List<BookEntity> getPageGenreBooks(Integer id, Integer offset, Integer limit) {
        return getBookEntity(getAllByGenreId(id, offset, limit));
    }
    public List<Book2GenreEntity> getGenre2BookByBookId(Integer bookId){
        return book2GenreRepository.findAllByBookId(bookId);
    }

    public List<GenreEntity> getGenreEntity (List<Book2GenreEntity> book2GenreEntity) {
        return book2GenreEntity.stream().map(Book2GenreEntity::getGenre).toList();
    }

    public List<BookEntity> getBookEntity(Page<Book2GenreEntity> book2GenreEntities) {
        List<BookEntity> bookEntities = new ArrayList<>();
        for (Book2GenreEntity book2Genre : book2GenreEntities) {
            bookEntities.add(book2Genre.getBook());
        }
        return bookEntities;
    }

}
