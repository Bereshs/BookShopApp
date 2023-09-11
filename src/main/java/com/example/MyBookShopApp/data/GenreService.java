package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.GenreRepository;
import com.example.MyBookShopApp.struct.genre.GenreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreEntity> getGenres(){
        return genreRepository.findAllByOrderById();
    }

    public List<GenreEntity> getGenresById(Integer id) {
        return genreRepository.findAllById(id);
    }
}
