package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.google.api.books.Item;
import com.example.MyBookShopApp.data.google.api.books.Root;
import com.example.MyBookShopApp.data.storage.BookPopularityRepository;
import com.example.MyBookShopApp.data.storage.BookRatingRepository;
import com.example.MyBookShopApp.data.storage.BookRepository;
import com.example.MyBookShopApp.errs.BookStorageApiWrongParametrException;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.book.links.BookPopularityEntity;
import com.example.MyBookShopApp.struct.book.links.BookRatingAndPopularity;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {


    private BookRepository bookRepository;
    private RestTemplate restTemplate;

    private BookRatingRepository bookRatingRepository;
    private BookPopularityRepository bookPopularityRepository;

    @Value("${google.books.api.key}")
    private String apiKey;

    @Autowired
    public BookService(BookRepository bookRepository,
                       BookRatingRepository bookRatingRepository,
                       BookPopularityRepository bookPopularityRepository,
                       RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.bookRatingRepository = bookRatingRepository;
        this.bookPopularityRepository = bookPopularityRepository;
        this.restTemplate = restTemplate;
    }

    public List<BookEntity> getPageOfGoogleBookApiSearchResult(String searchWord, Integer offset, Integer limit) {
        String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes" +
                "?q=" + searchWord +
                "&key=" + apiKey +
                "&filter=paid-ebooks" +
                "&startIndex=" + offset +
                "&maxResult=" + limit;

        Root root = restTemplate.getForEntity(REQUEST_URL, Root.class).getBody();
        ArrayList<BookEntity> list = new ArrayList<>();
        if (root != null) {
            for (Item item : root.getItems()) {
                BookEntity book = new BookEntity();
                if (item.getVolumeInfo() != null) {
                    //        book.setAuthor(new Author(item.getVolumeInfo().getAuthors()));
                    book.setTitle(item.getVolumeInfo().getTitle());
                    book.setImage(item.getVolumeInfo().getImageLinks().getThumbnail());
                }
                if (item.getSaleInfo() != null) {
                    book.setPrice((int) item.getSaleInfo().getRetailPrice().getAmount());
                    Double oldPrice = item.getSaleInfo().getListPrice().getAmount();
                    //  book.setPriceOld(oldPrice.intValue());
                }
                list.add(book);
            }
        }
        return list;

    }

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }


    public List<BookEntity> getBooksByTitle(String title) throws BookStorageApiWrongParametrException {
        if (title.length() <= 1) {
            throw new BookStorageApiWrongParametrException("Wrong values passed to one or more parameters");
        } else {
            List<BookEntity> data = bookRepository.findBooksByTitleContaining(title);
            if (data.size() > 0) {
                return data;
            } else {
                throw new BookStorageApiWrongParametrException("No data found with specified parameters");
            }
        }
    }

    public List<BookEntity> getBooksByPriceBetween(int min, int max) {
        return bookRepository.findBooksByPriceBetween(min, max);
    }

    public List<BookEntity> getBooksByPrice(int price) {
        return bookRepository.findBooksByPriceIs(price);
    }

    public List<BookEntity> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public List<BookEntity> getBooksWithMaxDiscount() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public BookPopularityEntity getPopularityById(Integer id) {
        Optional<BookPopularityEntity> result = bookPopularityRepository.findById(id);
        return result.orElseGet(BookPopularityEntity::new);
    }

    public List<BookEntity> getPageOfRecommendedBook(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return getBookEntity(bookRatingRepository.findAllByOrderByRating(nextPage));
    }

    public Page<BookEntity> getPageOfRecommendedBookNoAuthor(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, nextPage);
    }

    public Page<BookEntity> getPageOfRecentBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllByOrderByPubDate(nextPage);
    }

    public List<BookEntity> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return getBookEntity(bookPopularityRepository.findAllByOrderByPopularity(nextPage));
    }

    public Page<BookEntity> getPageOfRecentBooksBetween(LocalDateTime from, LocalDateTime to, Integer offset, Integer limit) {
        Pageable nexPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateBetween(from, to, nexPage);
    }

    public BookEntity getBookBySlug(String slug) {
        return bookRepository.findBookBySlug(slug);
    }

    public void save(BookEntity book) {
        bookRepository.save(book);
    }

    public List<BookEntity> getBookEntity(Page<? extends BookRatingAndPopularity> list) {
        List<BookEntity> result = new ArrayList<>();
        for (BookRatingAndPopularity element : list) {
            result.add(element.getBook());
        }
        return result;
    }

    public List<BookEntity> getBooksBySlugIn(String[] slugs) {
        return bookRepository.findBooksBySlugIn(slugs);
    }

    public BookEntity getBookById(Integer id) {
        return bookRepository.findById(id).get();
    }

    public Integer getRatingBookById(Integer id) {
        return bookRatingRepository.findRatingByBookId(id);
    }

    public BookEntity getById(Integer id) {
        return bookRepository.getById(id);
    }
}
