package com.example.News.Feed.Service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootTest
@EnableCaching
@EnableDiscoveryClient
class NewsFeedServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
