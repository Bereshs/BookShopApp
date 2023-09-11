package com.example.MyBookShopApp.struct.book.links;

import com.example.MyBookShopApp.struct.author.AuthorEntity;
import com.example.MyBookShopApp.struct.book.BookEntity;

import javax.persistence.*;
import java.awt.print.Book;

@Entity
@Table(name = "book2author")
public class Book2AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name="book_id", referencedColumnName = "id")
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private AuthorEntity author;

    @Column(columnDefinition = "INT NOT NULL  DEFAULT 0")
    private int sortIndex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }
}
