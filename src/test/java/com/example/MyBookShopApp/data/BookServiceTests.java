package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.BookRepository;
import com.example.MyBookShopApp.errs.BookStorageApiWrongParametrException;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.book.links.BookPopularityEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTests {

    private final BookService bookService;

    @MockBean
    private final BookRepository bookRepository;

    @Autowired
    BookServiceTests(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }


    @Test
    void getBooksDataTest() {
        BookEntity firstBook = new BookEntity();
        firstBook.setId(1);
        firstBook.setDescription("some description");
        firstBook.setTitle("some title");

        BookEntity secondBook = new BookEntity();
        secondBook.setId(2);
        secondBook.setDescription("second description");
        secondBook.setTitle("second title");

        Mockito.doReturn(Arrays.asList(firstBook, secondBook)).when(bookService).getBooksData();
        List<BookEntity> books = bookService.getBooksData();
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertTrue(books.contains(firstBook) && books.contains(secondBook));

    }

    @Test
    void getBooksByTitleTest() throws BookStorageApiWrongParametrException {
        String title = "some title";
        BookEntity book = new BookEntity();
        book.setId(1);
        book.setDescription("some description");
        book.setTitle(title);
        Mockito.doReturn(List.of(book)).when(bookService).getBooksByTitle(title);
        List<BookEntity> books = bookService.getBooksByTitle(title);
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(title, books.get(0).getTitle());
    }

    @Test
    void getCalculatePopularityBooksTest() {
        BookPopularityEntity popularity = bookService.getPopularityById(1);
        Double calculate = 0.7 * popularity.getCart() + 0.4 * popularity.getPostponed() + popularity.getBought();
        assertEquals(popularity.getPopularity(), calculate);
    }

    @Test
    void getPageOfPopularBooksTest() {
        List<BookEntity> books = bookService.getPageOfPopularBooks(5, 1);
        assertNotNull(books);
        assertFalse(books.isEmpty());
        for (int i = 1; i < books.size(); i++) {
            assert (bookService.getRatingBookById(books.get(i - 1).getId()) > bookService.getRatingBookById(books.get(i).getId()));
        }
    }

    @Test
    void getPageOfRecommendedBook() {
        List<BookEntity> books = bookService.getPageOfRecommendedBook(5,1);
        assertNotNull(books);
        assertFalse(books.isEmpty());
        for (int i = 1; i < books.size(); i++) {
            assert (bookService.getRatingBookById(books.get(i - 1).getId()) > bookService.getRatingBookById(books.get(i).getId()));
        }
    }

    @Test
    void saveBookTest() {
        BookEntity book = new BookEntity();
        book.setDescription("description");
        book.setTitle("title");
        book.setPrice(100);
        book.setPubDate(LocalDateTime.now());
        book.setSlug("slugBook");
        bookService.save(book);
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
    }
}
