package com.example.MyBookShopApp.errs;


import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class NoUserFoundException extends Exception {
    public NoUserFoundException(String s) {
        super(s);
    }
}
