package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.AuthorRepository;
import com.example.MyBookShopApp.struct.author.AuthorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorEntity getAuthor(int id) {
        return authorRepository.getById(id);
    }

    public Map<String, List<AuthorEntity>> getAuthorsMap() {
        List<AuthorEntity> authors = authorRepository.findAll();

        return
                authors.stream().collect(Collectors.groupingBy((AuthorEntity a) -> {
                    return a.getName().substring(0, 1);
                }));
    }

}
