package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.struct.book.BookEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
//@TestPropertySource("/application-test.properties")
class BookRepositoryTests {
    private final BookRepository bookRepository;

    @Autowired
    BookRepositoryTests(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBookByTitleContaining() {
        String token="os";
        List<BookEntity> booksListByTitleContaining = bookRepository.findBooksByTitleContaining(token);
        assertNotNull(booksListByTitleContaining);
        assertFalse(booksListByTitleContaining.isEmpty());
        booksListByTitleContaining.forEach(book -> {
            assertThat(book.getTitle().contains(token)); ///возможно не та библиотека
        });
    }

    @Test
    void findAllBestsellers() {
        List<BookEntity> bestsellersBook =  bookRepository.getBestsellers();
        assertNotNull(bestsellersBook);
        assertFalse(bestsellersBook.isEmpty());
        assertThat(bestsellersBook.size()).isGreaterThan(1);
        bestsellersBook.forEach(book -> {
            assertThat(book.isBestseller());
        });
    }
}