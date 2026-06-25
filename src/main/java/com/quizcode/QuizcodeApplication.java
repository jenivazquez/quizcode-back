package com.quizcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableRetry
@EnableAsync
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class QuizcodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizcodeApplication.class, args);
	}

}
