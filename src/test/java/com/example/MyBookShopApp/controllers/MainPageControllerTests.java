package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.security.BookstoreUserDetailsService;
import io.swagger.models.Model;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@TestPropertySource("/application-test.properties")
class MainPageControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    MainPageControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void mainPageAccessTest() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(content().string(containsString("ALL RIGHTS RESERVED")))
                .andExpect(status().isOk());
    }

    @Test
    public void accessOnlyAuthorizedPageFailTest() throws Exception {
        mockMvc.perform(get("/my"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/signin"));
    }

    @Test
    public void correctLoginTest() throws Exception {
        mockMvc.perform(formLogin("/signin").user("bereshs9@gmail.com").password("1234567"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void correctLoginPhoneTest() throws Exception {
        mockMvc.perform(formLogin("/signin").user("79090000000").password("1234567"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithUserDetails("bereshs9@gmail.com")
    void logoutTest() throws Exception {
        mockMvc.perform(get("/signin?logout=true"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("index"));
    }


    @Test
    @WithUserDetails("bereshs9@gmail.com")
    public void authenticatedTestToProfilePageTest() throws Exception {
        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/header/div[1]/div/div/div[3]/div/a[4]/span[1]").string("Дмитрий Петров"));
    }

    @Test
    public void searchQueryTest() throws Exception {
        mockMvc.perform(get("/search/Galium"))
                .andDo(print())
                .andExpect(xpath("/html/body/div/div/main/div[2]/header/h2").string("Найдено 9 книг"));
    }

    @Test
    public void specificTagPageTest() throws Exception {
        mockMvc.perform(get("tags/1"))
                .andDo(print())
                .andExpect(xpath("/html/body/div[2]/div/main/ul/li[3]").string("Glory Road"));
    }
}