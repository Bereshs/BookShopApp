package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.*;
import com.example.MyBookShopApp.data.dto.BookDto;
import com.example.MyBookShopApp.data.dto.ReviewDto;
import com.example.MyBookShopApp.data.storage.BookReviewLikesRepository;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.book.review.BookReviewEntity;
import com.example.MyBookShopApp.struct.book.review.BookReviewLikeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookService bookService;
    private final BookRatingService bookRatingService;

    private final Tag2BookService tag2BookService;

    private final ResourceStorage storage;
    private final BookFilesService bookFileTypeService;


    private final BookReviewLikesService bookReviewLikesService;

    private final BookReviewService bookReviewService;


    @Autowired
    public BooksController(BookService bookService, BookRatingService bookRatingService, Tag2BookService tag2BookService, ResourceStorage storage, BookFilesService bookFileTypeService1, BookReviewLikesService bookReviewLikesService, BookReviewService bookReviewService) {
        this.bookService = bookService;
        this.bookRatingService = bookRatingService;
        this.tag2BookService = tag2BookService;

        this.storage = storage;
        this.bookFileTypeService = bookFileTypeService1;
        this.bookReviewLikesService = bookReviewLikesService;
        this.bookReviewService = bookReviewService;
    }


    @GetMapping("/{slug}")
    public String BookPage(@PathVariable(value = "slug") String slug, Model model) {
        BookDto bookDto = new BookDto();
        bookDto.setBook(bookService.getBookBySlug(slug));
        Integer bookId = bookDto.getBook().getId();
        bookDto.setBook2TagEntity(tag2BookService.getTagEntity(tag2BookService.getTags2BookByBookId(bookId)));
        bookDto.setRating(bookRatingService.getRatingByBookId(bookId));
        model.addAttribute("bookDto", bookDto);
        model.addAttribute("filesToDownload", bookFileTypeService.getFilesByBookId(bookId));
        model.addAttribute("starRating", bookRatingService.getBookStars(bookId));
        model.addAttribute("reviewList", getReviewList(bookId));
        return "/books/slug";
    }


    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@PathVariable(value = "slug") String slug, @RequestParam("file") MultipartFile file) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        BookEntity bookToUpdate = bookService.getBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookService.save(bookToUpdate);
        String result = "redirect:/books/" + slug;
        Logger.getLogger(BooksController.class.getName()).info(result);
        return result;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookfile(@PathVariable(value = "hash") String hash) throws IOException {

        Path path = storage.getBookFilePath(hash);
        MediaType mediaType = storage.getBookFileMime(hash);
        byte[] data = storage.getBookFileByteArray(hash);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    public List<ReviewDto> getReviewList(Integer bookId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<ReviewDto> reviewList = new ArrayList<>();
        List<BookReviewEntity> reviewEntityList = bookReviewService.findAllByBookId(bookId);
        reviewEntityList.forEach(bookReviewEntity -> {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setBook_id(bookReviewEntity.getBookId());
            reviewDto.setText(bookReviewEntity.getText());
            reviewDto.setId(bookReviewEntity.getId());
            reviewDto.setTime(bookReviewEntity.getTime().format(formatter));
            reviewDto.setUserName(bookReviewEntity.getUser().getName());
            List<BookReviewLikeEntity> likesList = bookReviewLikesService.findAllByReviewId(bookReviewEntity.getId());
            reviewDto.setLike((int) likesList.stream().filter(c -> c.getValue()>0).count());
            reviewDto.setDislike((int) likesList.stream().filter(c -> c.getValue()<0).count());
            reviewList.add(reviewDto);
        });
        return reviewList;
    }


}
