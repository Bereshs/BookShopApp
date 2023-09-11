package com.example.MyBookShopApp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AllPagesTest {
    private static ChromeDriver driver;

    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.chrome.driver", "/home/bereshs/MyProjects/chromeDriver/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver=new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(8000, TimeUnit.MILLISECONDS);
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }


    @Test
    public void  allPagesTest() throws InterruptedException {
        String url="http://localhost:8085/";
        AllPages mainPage=new AllPages(driver);
        mainPage.setUrl(url);
        mainPage.callPage().pause();
        List<String> elements = mainPage.getNavigateLinks().stream().map(webElement -> {return webElement.getAttribute("href");}).toList();
        for(String element : elements) {
            mainPage.setUrl(element);
            mainPage.callPage();
            mainPage.pause();
            assertTrue(driver.getPageSource().contains("BOOKSHOP"));

            List<WebElement> linksList = mainPage.getAllLinks();
            int linkCounter = (int) (((double)linksList.size())/2);
            mainPage.setUrl(linksList.get(linkCounter).getAttribute("href"));
            mainPage.callPage();
            mainPage.pause();
            assertTrue(driver.getPageSource().contains("BOOKSHOP"));
        }
    }

}