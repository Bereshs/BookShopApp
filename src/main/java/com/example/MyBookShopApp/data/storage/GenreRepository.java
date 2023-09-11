package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    List<GenreEntity> findAllByOrderById();

    List<GenreEntity> findAllById(Integer id);
}
