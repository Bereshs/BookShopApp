package com.example.MyBookShopApp.struct.book.links;

import com.example.MyBookShopApp.struct.book.BookEntity;
import liquibase.pro.packaged.I;

import javax.persistence.*;

@Entity
@Table(name = "book_popularity")
public class BookPopularityEntity implements BookRatingAndPopularity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne
    @JoinColumn(name = "book_id")
    BookEntity book;
    //    Integer book_id;
    Integer cart;
    Integer postponed;
    Integer bought;

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

    public Integer getCart() {
        return cart;
    }

    public void setCart(Integer cart) {
        this.cart = cart;
    }

    public Integer getPostponed() {
        return postponed;
    }

    public void setPostponed(Integer postponed) {
        this.postponed = postponed;
    }

    public Integer getBought() {
        return bought;
    }

    public void setBought(Integer bought) {
        this.bought = bought;
    }

    public Double getPopularity() {
        return 0.7 * getCart() + 0.4 * getPostponed() + getBought();
    }

}
