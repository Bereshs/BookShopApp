package com.example.MyBookShopApp.struct.tags;

import com.example.MyBookShopApp.struct.book.BookEntity;

import javax.persistence.*;

@Table(name = "tag2book")
@Entity
public class Tag2BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
    @JoinColumn(name = "book_id")
    BookEntity book;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    TagEntity tag;
    //int tagId;


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

    public TagEntity getTag() {
        return tag;
    }

    public void setTag(TagEntity tag) {
        this.tag = tag;
    }
}
