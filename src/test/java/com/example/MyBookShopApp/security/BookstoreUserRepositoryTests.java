package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.struct.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookstoreUserRepositoryTests {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    BookstoreUserRepositoryTests(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Test
    public void testAddNewUser() {
        UserEntity user = new UserEntity();
        user.setPassword("12345678");
        user.setPhone("70004000000");
        user.setName("Vasiliy");
        user.setEmail("vasiliy@mail.ru");
        user.setHash(String.valueOf(user.getEmail().hashCode()));
        user.setRegTime(LocalDateTime.now());
        assertNotNull(bookstoreUserRepository.save(user));
    }
}