package com.example.MyBookShopApp.struct.book.links;

import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.genre.GenreEntity;
import liquibase.pro.packaged.K;

import javax.persistence.*;

@Entity
@Table(name = "book2genre")
public class Book2GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(columnDefinition = "INT NOT NULL")
//    private int bookId;
    @ManyToOne
    @JoinColumn(name="book_id")
    private BookEntity book;

    //@Column(columnDefinition = "INT NOT NULL")
    @ManyToOne
    @JoinColumn(name="genre_id")
    private GenreEntity genre;
   // private int genreId;

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public GenreEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
    }
}
