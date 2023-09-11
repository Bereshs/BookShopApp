package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.errs.NoUserFoundException;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookstoreUserDetailsService implements UserDetailsService {

    private final BookstoreUserRepository bookstoreUserRepository;


    @Autowired
    public BookstoreUserDetailsService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        UserEntity user = bookstoreUserRepository.findUserByEmail(s);
        if (user != null) {
            return new BookstoreUserDetails(user);
        }
        return null;
    }


}
