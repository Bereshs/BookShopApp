package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.UserService;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.data.storage.UserRepository;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ModelController {
    private final JWTUtil jwtUtil;

    private final UserService userService;

    @Autowired
    public ModelController(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserEntity user(@CookieValue(name = "token", required = false) String token) {
        if (token==null || jwtUtil.isTokenExpired(token)) {
            return new UserEntity();
        }
        return userService.getByEmail(jwtUtil.extractUsername(token));
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("cartAmount")
    public Integer getCartAmount(@CookieValue(name = "cartContents", required = false
    ) String cartContents) {
        if (cartContents == null || cartContents.equals("")) {
            return 0;
        }
        return cartContents.split("/").length;
    }

    @ModelAttribute("postponedAmount")
    public Integer postponedAmount(@CookieValue(name = "keptContents", required = false
    ) String cartContents) {
        if (cartContents == null || cartContents.equals("")) {
            return 0;
        }
        return cartContents.split("/").length;
    }

    @ModelAttribute("status")
    public String getAuthorization() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser")) {
            return "unauthorized";
        }
        return "authorized";
    }


}
