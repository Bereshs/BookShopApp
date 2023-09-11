package com.example.MyBookShopApp.controllers;


import com.example.MyBookShopApp.data.Tag2BookService;
import com.example.MyBookShopApp.data.storage.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TagsController {
    Tag2BookService tag2BookService;


    @Autowired
    public TagsController(Tag2BookService tag2BookService) {
        this.tag2BookService = tag2BookService;
    }

    @GetMapping("/tags/{id}")
    public String tagsPage(@PathVariable(value = "id", required = false) Integer id, Model model) {

        model.addAttribute("categoryName", tag2BookService.getByTagId(id).getName());
        model.addAttribute("booksList", tag2BookService.getPageOfBooksByTag(id, 0, 20));
        model.addAttribute("refreshid", id);
        return "/tags/index";
    }
}
