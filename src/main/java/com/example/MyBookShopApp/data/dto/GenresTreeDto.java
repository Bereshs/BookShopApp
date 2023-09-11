package com.example.MyBookShopApp.data.dto;

import java.util.ArrayList;
import java.util.List;

public class GenresTreeDto {
    private long id;
    private String title;
    private long countBooks;
    private List<GenresTreeDto> elements;

    public GenresTreeDto(String title) {
        this.title = title;
        elements = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCountBooks() {
        return countBooks;
    }

    public void setCountBooks(long countBooks) {
        this.countBooks = countBooks;
    }
    public GenresTreeDto getElement(long id) {
        if (id == getId()) {
            return this;
        }
        for (GenresTreeDto element : elements) {
            GenresTreeDto result = element.getElement(id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    public void addElement(GenresTreeDto element) {
        elements.add(element);
    }

    public List<GenresTreeDto> getElements() {
        return elements;
    }

    public long getElementsSize() {
        return elements.size();
    }
}
