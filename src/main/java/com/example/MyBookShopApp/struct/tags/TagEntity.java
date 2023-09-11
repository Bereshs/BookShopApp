package com.example.MyBookShopApp.struct.tags;

import com.example.MyBookShopApp.struct.book.BookEntity;

import javax.persistence.*;

@Entity
@Table(name = "tag")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
