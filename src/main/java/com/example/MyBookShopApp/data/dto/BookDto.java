package com.example.MyBookShopApp.data.dto;

import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.book.links.BookRatingEntity;
import com.example.MyBookShopApp.struct.tags.TagEntity;

import java.util.List;

public class BookDto {
    BookEntity book;

    Integer rating;

    List<TagEntity> book2TagEntity;

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public List<TagEntity> getBook2TagEntity() {
        return book2TagEntity;
    }

    public void setBook2TagEntity(List<TagEntity> book2GenreEntity) {
        this.book2TagEntity = book2GenreEntity;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
