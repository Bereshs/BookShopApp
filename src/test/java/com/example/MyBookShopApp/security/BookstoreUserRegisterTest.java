package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JWTUtil;
import com.example.MyBookShopApp.struct.user.UserEntity;
import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWT;
import io.jsonwebtoken.Jwts;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestMethod;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
//@TestPropertySource("/application-test.properties")
class BookstoreUserRegisterTest {
    private final BookstoreUserRegister userRegister;
    private final PasswordEncoder passwordEncoder;
    private RegistrationForm registrationForm;

    @MockBean
    private BookstoreUserRepository bookstoreUserRepositoryMock;

    @MockBean
    BookstoreUserRegister userRegisterMock;

    @MockBean
    JWTUtil jwtUtilMock;

    @Autowired
    BookstoreUserRegisterTest(BookstoreUserRegister userRegister, PasswordEncoder passwordEncoder) {
        this.userRegister = userRegister;
        this.passwordEncoder = passwordEncoder;
    }


    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("mail@test.ru");
        registrationForm.setName("Василий теркин");
        registrationForm.setPass("1234567");
        registrationForm.setPhone("79090000000");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerUser() {
        UserEntity user = userRegister.registerUser(registrationForm);
        assertNotNull(user);
        assertTrue(passwordEncoder.matches(registrationForm.getPass(), user.getPassword()));
        assertTrue(CoreMatchers.is(user.getPhone()).matches(registrationForm.getPhone()));
        assertTrue(CoreMatchers.is(user.getName()).matches(registrationForm.getName()));
        assertTrue(CoreMatchers.is(user.getEmail()).matches(registrationForm.getEmail()));
        Mockito.verify(bookstoreUserRepositoryMock, Mockito.times(1)).save(Mockito.any(UserEntity.class));
    }

    @Test
    void registerNewUserFail() {
        Mockito.doReturn(new UserEntity())
                .when(bookstoreUserRepositoryMock)
                .findUserByEmail(registrationForm.getEmail());
        UserEntity user = userRegister.registerUser(registrationForm);
        assertNull(user);
    }

}