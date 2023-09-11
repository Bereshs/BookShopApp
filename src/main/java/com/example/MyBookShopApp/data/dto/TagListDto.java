package com.example.MyBookShopApp.data.dto;

public class TagListDto implements Comparable<TagListDto> {

    String name;
    Integer id;
    Integer count;
    String sizeClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSizeClass() {
        return sizeClass;
    }

    public void setSizeClass(String sizeClass) {
        this.sizeClass = sizeClass;
    }

    @Override
    public int compareTo(TagListDto o) {
        return this.getCount().compareTo(o.getCount());
    }
}
