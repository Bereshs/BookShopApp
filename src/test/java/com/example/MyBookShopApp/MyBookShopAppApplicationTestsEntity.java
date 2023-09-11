package com.example.MyBookShopApp;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MyBookShopAppApplicationTestsEntity {


	@Value("${auth.secret}")
	String authSecret;

	private final MyBookShopAppApplication appApplication;

	@Autowired
	MyBookShopAppApplicationTestsEntity(MyBookShopAppApplication appApplication) {
		this.appApplication = appApplication;
	}

	@Test
	void contextLoadsTest() {
		assertNotNull(appApplication);
	}

	@Test
	void verifyAuthSecretTest() {
		assertThat(authSecret, Matchers.containsString("skillbox"));
	}

}
