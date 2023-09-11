package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.*;
import com.example.MyBookShopApp.data.dto.GenresTreeDto;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.struct.genre.GenreEntity;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GenresController {
    private final GenreService genreService;
    private final Book2GenreService book2GenreService;

    @Autowired
    public GenresController(GenreService genreService, Book2GenreService book2GenreService) {
        this.genreService = genreService;
        this.book2GenreService = book2GenreService;
    }

    @GetMapping(value = {"/genres", "/genres/index"})
    public String genresPage(Model model) {
        model.addAttribute("genresTreeDto", getGenresTreeDto());
        return "/genres/index";
    }

    @GetMapping("/genres/{id}")
    public String genresSlugPage(@PathVariable(value = "id", required = false) Integer id, Model model) {
        model.addAttribute("category", genreService.getGenresById(id).get(0).getName());
        model.addAttribute("booksList", book2GenreService.getPageGenreBooks(id,0,20));
        model.addAttribute("refreshid", id);
        return "/genres/slug";
    }

    public GenresTreeDto getGenresTreeDto() {
        GenresTreeDto result = new GenresTreeDto("root");
        result.setId(0);
        List<GenreEntity> genresList = genreService.getGenres();
        for (GenreEntity element : genresList) {
            GenresTreeDto parentElement = result.getElement(element.getParentId());
            GenresTreeDto newElement = new GenresTreeDto(element.getName());
            newElement.setId(element.getId());
            newElement.setCountBooks(element.getCountBook());
            parentElement.addElement(newElement);
        }
        return result;
    }

}
