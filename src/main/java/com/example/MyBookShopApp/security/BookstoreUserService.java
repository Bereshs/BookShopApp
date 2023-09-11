package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserService {
    private final BookstoreUserRepository bookstoreUserRepository;

    public BookstoreUserService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    public UserEntity findUserByEmail(String email) {
        return bookstoreUserRepository.findUserByEmail(email);
    }
}
