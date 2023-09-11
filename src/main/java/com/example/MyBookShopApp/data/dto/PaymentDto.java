package com.example.MyBookShopApp.data.dto;

import io.swagger.models.auth.In;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentDto {
    private LocalDateTime time;
    private Integer sum;
    private String description;
    private String slug;

    public String getTime() {
        String datePattern = "dd MMMM yyyy HH:mm";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(datePattern);
        return europeanDateFormatter.format(time);
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
