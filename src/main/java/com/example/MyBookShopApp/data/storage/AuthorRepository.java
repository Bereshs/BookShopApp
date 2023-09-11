package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.author.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

}
