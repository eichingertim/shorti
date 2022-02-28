package me.timeichinger.shortiservice;

import me.timeichinger.shortiservice.model.ShortUrl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("me.timeichinger.shortiservice.model")
public class ShortiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortiServiceApplication.class, args);
	}

}
