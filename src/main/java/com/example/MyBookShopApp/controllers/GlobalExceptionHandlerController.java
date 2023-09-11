package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.dto.ApiResponse;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.errs.NoUserFoundException;
import com.example.MyBookShopApp.errs.UserAlreadyExistException;
import com.example.MyBookShopApp.security.AuthUserController;
import com.example.MyBookShopApp.security.BookstoreUserDetailsService;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.user.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandlerController extends Exception {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }
    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<ApiResponse<UserEntity>> handleNoUserFoundException (Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND, "Wrong userName", exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserAlreadyExistException(UserAlreadyExistException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userError", e.getMessage());
        Logger.getLogger(AuthUserController.class.getName()).info("ex "+e.getMessage());

        return "redirect:/signup";
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public String handleExpiredJwtException(Exception e, HttpServletResponse httpServletResponse) {
        Logger.getLogger(GlobalExceptionHandlerController.class.getName()).info(e.getMessage());
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
        return "signin";
    }
}
