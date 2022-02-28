package me.timeichinger.shortiservice.controller;

import me.timeichinger.shortiservice.model.ShortUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShortUrlControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createShortUrlWithPredefinedShortStringShouldReturnShortUrl() {
        String originUrl = "lol";
        String shortUrlStr = "google";

        String request = String.format("http://localhost:%d/%s?shortUrlStr=%s",port, originUrl, shortUrlStr);

        ShortUrl url = this.restTemplate.postForObject(request, null,
                ShortUrl.class);

        System.out.println(url);

        assertThat(url.getId()).isNotNull();
        assertThat(url.getOriginUrl()).isEqualTo(originUrl);
        assertThat(url.getShortUrl()).isEqualTo(shortUrlStr);
        assertThat(url.getCreatedAt()).isNotNull();
    }

}
