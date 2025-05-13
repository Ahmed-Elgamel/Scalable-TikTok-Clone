package com.example.News.Feed.Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class NewsFeedServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsFeedServiceApplication.class, args);
	}

}
