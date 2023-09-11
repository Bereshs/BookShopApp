package com.example.MyBookShopApp.struct.book.review;

import com.example.MyBookShopApp.struct.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT NOT NULL")
    private int bookId;

    //@Column(columnDefinition = "INT NOT NULL")
    //private int userId;
    @ManyToOne
    @JoinColumn(name="user_id")
    UserEntity user;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
