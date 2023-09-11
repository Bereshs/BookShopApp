package com.example.MyBookShopApp.data.dto;

import java.util.List;

public class RatingStarDto {
    private List<Integer> rating;

    public List<Integer> getRating() {
        return rating;
    }

    public void setRating(List<Integer> rating) {
        this.rating = rating;
    }

    public Integer getSize() {
        return rating.size();
    }
}
