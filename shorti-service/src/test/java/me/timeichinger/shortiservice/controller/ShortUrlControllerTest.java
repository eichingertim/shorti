package me.timeichinger.shortiservice.controller;

import com.google.gson.JsonObject;
import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.utils.ShortUrlList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShortUrlControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
     void createShortUrlWithPredefinedShortStringShouldReturnShortUrl() {
        String originUrl = "https://google.com";
        String shortUrlStr = "google";

        String request = String.format("http://localhost:%d/shortUrl?shortUrlStr=%s",port, shortUrlStr);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", originUrl);

        ShortUrl url = this.restTemplate.postForObject(request, jsonObject, ShortUrl.class);

        assertThat(url.getId()).isNotNull();
        assertThat(url.getOriginUrl()).isEqualTo(originUrl);
        assertThat(url.getShortUrl()).isEqualTo(shortUrlStr);
        assertThat(url.getCreatedAt()).isNotNull();
    }

    @Test
    void createShortUrlShouldReturnShortUrl() {
        String originUrl = "https://google.com";

        String request = String.format("http://localhost:%d/shortUrl",port);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", originUrl);

        ShortUrl url = this.restTemplate.postForObject(request, jsonObject, ShortUrl.class);

        assertThat(url.getId()).isNotNull();
        assertThat(url.getOriginUrl()).isEqualTo(originUrl);
        assertThat(url.getShortUrl()).isNotNull();
        assertThat(url.getCreatedAt()).isNotNull();
    }

    @Test
    void updateShortUrlShouldReturnUpdatedShortUrl() {
        ShortUrl createdUrl = shortenUrl();
        String newOriginUrl = "https://timeichinger.me";
        String updateRequest = String.format("http://localhost:%d/%s", port, createdUrl.getId());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", newOriginUrl);

        ShortUrl updatedUrl = this.restTemplate.patchForObject(updateRequest, jsonObject, ShortUrl.class);

        assertThat(updatedUrl.getId()).isNotNull();
        assertThat(updatedUrl.getOriginUrl()).isEqualTo(newOriginUrl);
        assertThat(updatedUrl.getShortUrl()).isEqualTo(createdUrl.getShortUrl());
    }

    @Test
    void deleteShortUrl() {
        ShortUrl createdUrl = shortenUrl();

        String getAllRequest = String.format("http://localhost:%d/all", port);
        List<ShortUrl> shortUrlListBefore = this.restTemplate.getForObject(getAllRequest, ShortUrlList.class);

        assertThat(shortUrlListBefore.size()).isEqualTo(1);

        String updateRequest = String.format("http://localhost:%d/%s", port, createdUrl.getId());
        this.restTemplate.delete(updateRequest);

        List<ShortUrl> shortUrlListAfter = this.restTemplate.getForObject(getAllRequest, ShortUrlList.class);
        assertThat(shortUrlListAfter.size()).isEqualTo(0);
    }

    public ShortUrl shortenUrl() {
        String originUrl = "https://google.com";

        String request = String.format("http://localhost:%d/shortUrl",port);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", originUrl);

        return this.restTemplate.postForObject(request, jsonObject, ShortUrl.class);
    }

}
