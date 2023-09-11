package com.example.MyBookShopApp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTests {

    private final MockMvc mockMvc;


    @Autowired
    CartControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void handleCartRequest() throws Exception {
        mockMvc.perform(get("/cart")
                        .cookie(new Cookie("cartContents", "01d8723158e6c30070b527de92dcdc77c6e314a7")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div/main/form/div[1]/div/div[2]/div[1]/div[2]/a").string("Galium watsonii (A. Gray) A. Heller"));
    }

    @Test
    void handleChangeBookStatus() throws Exception {
        mockMvc.perform(post("/books/changeBookStatus/cart/01d8723158e6c30070b527de92dcdc77c6e314a7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(cookie().value("cartContents", "01d8723158e6c30070b527de92dcdc77c6e314a7"));
    }

}