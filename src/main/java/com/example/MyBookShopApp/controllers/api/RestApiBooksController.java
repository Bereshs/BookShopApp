package com.example.MyBookShopApp.controllers.api;

import com.example.MyBookShopApp.data.Book2AuthorService;
import com.example.MyBookShopApp.data.Book2GenreService;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.Tag2BookService;
import com.example.MyBookShopApp.data.dto.*;
import com.example.MyBookShopApp.errs.BookStorageApiWrongParametrException;
import com.example.MyBookShopApp.struct.book.BookEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@Api("Books api controller")
public class RestApiBooksController {

    private final BookService bookService;

    private final Book2GenreService book2GenreService;
    private final Tag2BookService tag2BookService;

    private final Book2AuthorService book2AuthorService;
    @Autowired
    public RestApiBooksController(BookService bookService, Book2GenreService book2GenreService, Tag2BookService tag2BookService, Book2AuthorService book2AuthorService) {
        this.bookService = bookService;
        this.book2GenreService = book2GenreService;
        this.tag2BookService = tag2BookService;
        this.book2AuthorService = book2AuthorService;
    }

    @GetMapping("/books/by-title")
    @ApiOperation("operation to get book list of bookshop by passed book title")
    public ResponseEntity<ApiResponse<BookEntity>> booksByTitle(@RequestParam("title") String title) throws BookStorageApiWrongParametrException {
        ApiResponse<BookEntity> response =  new ApiResponse<>();
        List<BookEntity> data =  bookService.getBooksByTitle(title);
        response.setDebugMessage("successful request");
        response.setMessage("data size :" +data.size());
        response.setStatus(HttpStatus.OK);
        response.setTimeStamp(LocalDateTime.now());
        response.setData(data);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/books/by-price-range")
    @ApiOperation("operation to get book list of bookshop by passed price range")
    public ResponseEntity<List<BookEntity>> booksByPriceRange(@RequestParam("min") int min, @RequestParam("max") int max) {
        return ResponseEntity.ok(bookService.getBooksByPriceBetween(min, max));
    }

    @GetMapping("/books/by-price")
    @ApiOperation("operation to get book list of bookshop by passed price")
    public ResponseEntity<List<BookEntity>> booksByPrice(@RequestParam("price") int price) {
        return ResponseEntity.ok(bookService.getBooksByPrice(price));
    }

    @GetMapping("/books/bestsellers")
    @ApiOperation("operation to get book bestsellers list of bookshop")
    public ResponseEntity<List<BookEntity>> booksBestsellers() {
        return ResponseEntity.ok(bookService.getBestsellers());
    }

    @GetMapping("/books/max-discount")
    @ApiOperation("operation to get book list of bookshop with max discount")
    public ResponseEntity<List<BookEntity>> booksMaxDiscount() {
        return ResponseEntity.ok(bookService.getBooksWithMaxDiscount());
    }

    @GetMapping("/books/popular/page")
    @ApiOperation("operation to get pageble popular books")
    public BooksPageDto pagedPopularBooks(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit));
    }

    @GetMapping("/books/recent/page")
    @ApiOperation("operation to get pageble recent books")
    public BooksPageDto pagedRecentBooks(@RequestParam("from") String fromString, @RequestParam("to") String toString,
                                         @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate from = LocalDate.parse(fromString, formatter);
        LocalDate to = LocalDate.parse(toString, formatter);

        return new BooksPageDto(bookService.getPageOfRecentBooksBetween(from.atTime(LocalTime.MIN), to.atTime(LocalTime.MAX), offset, limit).getContent());
    }


    @GetMapping("/books/genre/{id}/page")
    @ApiOperation("operation to get pageble books by genre")
    public BooksPageDto getNextGenrePage(@PathVariable(value = "id", required = false) Integer id,
                                         @RequestParam(value = "offset") Integer offset,
                                         @RequestParam(value = "limit") Integer limit) {
        return new BooksPageDto(book2GenreService.getPageGenreBooks(id, offset, limit));
    }

    @GetMapping("/books/tag/{id}/page")
    @ApiOperation("operation to get pageble books by tag")
    public BooksPageDto getNextTagPage(@PathVariable(value = "id", required = false) Integer id,
                                       @RequestParam(value = "offset") Integer offset,
                                       @RequestParam(value = "limit") Integer limit) {
        return new BooksPageDto(tag2BookService.getPageOfBooksByTag(id, offset, limit));
    }
    @GetMapping("/books/author/{id}/page")
    @ApiOperation("operation to get pageble books by author")
    public BooksPageDto getNextAuthorPage(@PathVariable(value = "id", required = false) Integer id,
                                       @RequestParam(value = "offset") Integer offset,
                                       @RequestParam(value = "limit") Integer limit) {
        return new BooksPageDto(book2AuthorService.getBookEntity(book2AuthorService.getPageOfBooksByAuthorId(id, offset, limit)));
    }

    @GetMapping("/books/tag/")
    @ApiOperation("operation to get all tags")
    public List<TagListDto> getTags() {
        return tag2BookService.getTagListDto();
    }


    @GetMapping("/books/search/page/{searchWord}")
    @ApiOperation("operation to search page books like searchword")
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit).getContent());

    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<BookEntity>> handleMissingServletRequestParameterException (Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Missing requre parameters", exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookStorageApiWrongParametrException.class)
    public ResponseEntity<ApiResponse<BookEntity>> handleBookStorageApiWrongParametrException(Exception exception) {
        return  new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "bad parameter value", exception), HttpStatus.BAD_REQUEST);
    }

}

