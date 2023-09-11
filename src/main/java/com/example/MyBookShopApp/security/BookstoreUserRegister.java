package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.aspects.annotations.UserSecurity;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Logger;


@Service
public class BookstoreUserRegister {

    private final BookstoreUserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private JWTUtil jwtUtil;


    @Autowired
    @UserSecurity
    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository
            , PasswordEncoder passwordEncoder
            , AuthenticationManager authenticationManager
            , BookstoreUserDetailsService bookstoreUserDetailsService
            , JWTUtil jwtUtil) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;

    }


    public UserEntity registerUser(RegistrationForm registrationForm) {
        UserEntity user = null;
        if (bookstoreUserRepository.findUserByEmail(registrationForm.getEmail()) == null) {
            user = new UserEntity();
            user.setEmail(registrationForm.getEmail());
            user.setName(registrationForm.getName());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
            user.setHash(String.valueOf(registrationForm.hashCode()));
            user.setRegTime(LocalDateTime.now());
            bookstoreUserRepository.save(user);
        }
        return user;
    }

    public Date getExpiration(String token) {
        return jwtUtil.extractExpiration(token);
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }


    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public UserEntity getCurrentUser() {
        BookstoreUserDetails bookstoreUserDetails = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return bookstoreUserDetails.getUser();
    }

}


