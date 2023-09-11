package com.example.MyBookShopApp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class AllPages {
    private ChromeDriver driver;
    private String url = "http://localhost:8085/";

    AllPages(ChromeDriver driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AllPages callPage() {
        driver.get(url);
        return this;
    }

    public AllPages pause() throws InterruptedException {
        Thread.sleep(2000);
        return this;
    }

    public AllPages clickHref(String linkText) {
        driver.findElement(new By.ByLinkText(linkText)).click();
        return this;
    }

    public AllPages clickBack() {
        driver.navigate().back();
        return this;
    }

    public List<WebElement> getNavigateLinks() {
        return driver.findElementsByClassName("menu-link");
    }

    public List<WebElement> getAllLinks() {
        return driver.findElementsByTagName("a");
    }

}
