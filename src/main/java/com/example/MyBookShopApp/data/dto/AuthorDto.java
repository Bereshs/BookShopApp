package com.example.MyBookShopApp.data.dto;

import java.util.ArrayList;
import java.util.List;

public class AuthorDto {
    private String name;
    private String descriptionVisible;

    private String descriptionHide;
    private String photo;

    public void setDescription(String description) {
        ArrayList<String> lines = new ArrayList<>(List.of(description.split(System.lineSeparator())));
        Integer counterSize = 0;
        StringBuilder visible = new StringBuilder();
        StringBuilder hide = new StringBuilder();
        for (String line : lines) {
            counterSize = counterSize + line.length();
            if (counterSize < 430) {
                visible.append("<p>" + line + "</p>");
            } else {
                hide.append("<p>" + line + "</p>");
            }
        }
        descriptionHide = hide.toString();
        descriptionVisible = visible.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionVisible() {
        return descriptionVisible;
    }

    public String getDescriptionHide() {
        return descriptionHide;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
