package com.example.MyBookShopApp.data.dto;

import com.example.MyBookShopApp.struct.user.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewDto {
    private Integer id;
    private Integer book_id;
    private String textMain;
    private String textHide;

    private String time;
    private String userName;
    private Integer like;
    private Integer dislike;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBook_id() {
        return book_id;
    }

    public void setBook_id(Integer book_id) {
        this.book_id = book_id;
    }

    public String getTextMain() {
        return textMain;
    }

    public String getTextHide() {
        return textHide;
    }

    public void setText(String text) {
        int viewTextCount = 600;
        int counter = 0;
        StringBuilder mainBuilder = new StringBuilder();
        StringBuilder hideBuilder = new StringBuilder();

        String[] lines = text.split(System.lineSeparator());
        for (int i = 0; i < lines.length; i++) {
            if (counter < viewTextCount) {
                mainBuilder.append(lines[i]).append(System.lineSeparator());
            } else {
                hideBuilder.append(lines[i]).append(System.lineSeparator());
            }
            counter = counter + lines[i].length();
        }
        this.textMain = mainBuilder.toString();
        this.textHide = hideBuilder.toString();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getDislike() {
        return dislike;
    }

    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }
}
