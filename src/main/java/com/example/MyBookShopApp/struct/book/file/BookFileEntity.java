package com.example.MyBookShopApp.struct.book.file;

import com.example.MyBookShopApp.struct.book.BookEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_file")
public class BookFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String path;
  //  @Column(columnDefinition = "INT NOT NULL")
    @ManyToOne
    @JoinColumn(name="type_id")
    //private int typeId;
    private BookFileTypeEntity fileTypeEntity;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BookFileTypeEntity getFileTypeEntity() {
        return fileTypeEntity;
    }

    public void setFileTypeEntity(BookFileTypeEntity fileTypeEntity) {
        this.fileTypeEntity = fileTypeEntity;
    }
}
