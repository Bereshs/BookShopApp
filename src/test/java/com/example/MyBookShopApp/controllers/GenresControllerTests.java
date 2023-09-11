package com.example.MyBookShopApp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
class GenresControllerTests {

    private final MockMvc mockMvc;

    GenresControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void genresPageTest() throws Exception {
        mockMvc.perform(get("genres/"))
                .andDo(print())
                .andExpect(xpath("/html/body/div/div/main/h1").string("Genre"));
    }

    @Test
    void genresSlugPageTest() throws Exception {
        mockMvc.perform(get("genres/1"))
                .andDo(print())
                .andExpect(xpath("/html/body/div/div/main/ul/li[3]").string("Crime|Horror|Mystery"));
    }

}