package com.example.MyBookShopApp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
class BooksControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    BooksControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void bookPageTest() throws Exception {
        mockMvc.perform(get("/books/9200f7141cce48b29e86fbb13309eb345e806e2b"))
                .andDo(print())
                .andExpect(xpath("/html/body/div/div/main/div/div[1]/div[2]/div[1]/h1").string("Ceiba Mill."));
    }



}