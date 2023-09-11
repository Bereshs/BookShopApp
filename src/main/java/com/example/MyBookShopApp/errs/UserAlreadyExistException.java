package com.example.MyBookShopApp.errs;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
