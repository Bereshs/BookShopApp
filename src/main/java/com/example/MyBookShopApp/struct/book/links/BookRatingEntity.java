package com.example.MyBookShopApp.struct.book.links;

import com.example.MyBookShopApp.struct.book.BookEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_rating")
public class BookRatingEntity implements BookRatingAndPopularity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;

    private Integer userId;
    private Integer rating;

    @Transient
    private Integer count;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
