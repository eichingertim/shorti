package me.timeichinger.shortiservice.controller;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.model.User;
import me.timeichinger.shortiservice.utils.ShortUrlList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShortUrlControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        signUpAndLoginUser();
    }

    @Test
     void createShortUrlWithAuthWithPredefinedShortStringShouldReturnShortUrl() {
        String originUrl = "https://google.com";
        String shortUrlStr = "google";

        String request = String.format("http://localhost:%d/shortUrl?shortUrlStr=%s",port, shortUrlStr);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", originUrl);

        HttpEntity<JsonObject> entity = new HttpEntity<>(jsonObject, headers);
        ShortUrl url = restTemplate.postForObject(request, entity, ShortUrl.class);

        assertThat(url.getId()).isNotNull();
        assertThat(url.getOriginUrl()).isEqualTo(originUrl);
        assertThat(url.getShortUrl()).isEqualTo(shortUrlStr);
        assertThat(url.getCreatedAt()).isNotNull();
        assertThat(url.getCreator()).isNotNull();
    }

    @Test
    void createShortUrlWithoutAuthWithPredefinedShortStringShouldReturn400() {
        String originUrl = "https://google.com";
        String shortUrlStr = "google";

        String request = String.format("http://localhost:%d/shortUrl?shortUrlStr=%s",port, shortUrlStr);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", originUrl);

        HttpEntity<JsonObject> requestEntity = new HttpEntity<>(jsonObject);
        ResponseEntity<String> lol = restTemplate.exchange(request, HttpMethod.POST,requestEntity, String.class);

        assertThat(lol.getStatusCode().is4xxClientError()).isEqualTo(true);

    }

    @Test
    void createShortUrlWithAuthShouldReturnShortUrl() {
        String originUrl = "https://google.com";

        String request = String.format("http://localhost:%d/shortUrl",port);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", originUrl);

        HttpEntity<JsonObject> entity = new HttpEntity<>(jsonObject, headers);
        ShortUrl url = restTemplate.postForObject(request, entity, ShortUrl.class);

        assertThat(url.getId()).isNotNull();
        assertThat(url.getOriginUrl()).isEqualTo(originUrl);
        assertThat(url.getShortUrl()).isNotNull();
        assertThat(url.getCreatedAt()).isNotNull();
        assertThat(url.getCreator()).isNotNull();
    }

    @Test
    void createShortUrlWithoutAuthShouldReturnShortUrl() {
        String originUrl = "https://google.com";

        String request = String.format("http://localhost:%d/shortUrl",port);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", originUrl);

        ShortUrl url = restTemplate.postForObject(request, jsonObject, ShortUrl.class);

        assertThat(url.getId()).isNotNull();
        assertThat(url.getOriginUrl()).isEqualTo(originUrl);
        assertThat(url.getShortUrl()).isNotNull();
        assertThat(url.getCreatedAt()).isNotNull();
        assertThat(url.getCreator()).isNull();
    }

    @Test
    void updateShortUrlShouldReturnUpdatedShortUrl() {
        ShortUrl createdUrl = shortenUrl();
        String newOriginUrl = "https://timeichinger.me";
        String updateRequest = String.format("http://localhost:%d/%s", port, createdUrl.getId());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", newOriginUrl);

        HttpEntity<JsonObject> entity = new HttpEntity<>(jsonObject, headers);
        ShortUrl updatedUrl = restTemplate.patchForObject(updateRequest, entity, ShortUrl.class);

        assertThat(updatedUrl.getId()).isNotNull();
        assertThat(updatedUrl.getOriginUrl()).isEqualTo(newOriginUrl);
        assertThat(updatedUrl.getShortUrl()).isEqualTo(createdUrl.getShortUrl());
    }

    @Test
    void deleteShortUrl() {
        ShortUrl createdUrl = shortenUrl();

        String getAllRequest = String.format("http://localhost:%d/all", port);
        HttpEntity entity = new HttpEntity<JsonObject>(headers);

        ResponseEntity<ShortUrlList> resBefore = restTemplate.exchange(
                getAllRequest,
                HttpMethod.GET,
                entity,
                ShortUrlList.class
        );

        assertThat(resBefore.getBody()).isNotNull();
        assertThat(resBefore.getBody().size()).isGreaterThan(0);
        int currSize = resBefore.getBody().size();

        String deleteReq = String.format("http://localhost:%d/%s", port, createdUrl.getId());
        restTemplate.exchange(
                deleteReq,
                HttpMethod.DELETE,
                entity,
                String.class
        );

        ResponseEntity<ShortUrlList> resAfter = restTemplate.exchange(
                getAllRequest,
                HttpMethod.GET,
                entity,
                ShortUrlList.class
        );

        assertThat(resAfter.getBody()).isNotNull();
        assertThat(resAfter.getBody().size()).isEqualTo(currSize-1);
    }

    private ShortUrl shortenUrl() {
        String originUrl = "https://google.com";

        String request = String.format("http://localhost:%d/shortUrl",port);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originUrl", originUrl);

        HttpEntity entity = new HttpEntity<>(jsonObject, headers);
        return restTemplate.postForObject(request, entity, ShortUrl.class);
    }

    private void signUpAndLoginUser() {
        JsonObject userSignUp = new JsonObject();
        userSignUp.addProperty("email", "test@mail.com");
        userSignUp.addProperty("username", "testuser123");
        userSignUp.addProperty("password", "password123");

        String signUpRequest = String.format("http://localhost:%d/sign-up",port);
        try {
            restTemplate.postForObject(signUpRequest, userSignUp, User.class);
        } catch (Exception e) {
            System.out.println("User already created");
        }


        JsonObject userLoginRequest = new JsonObject();
        userLoginRequest.addProperty("username", "testuser123");
        userLoginRequest.addProperty("password", "password123");

        String loginRequest = String.format("http://localhost:%d/sign-in",port);
        TokenWrapper wrapper = restTemplate.postForObject(loginRequest, userLoginRequest, TokenWrapper.class);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + wrapper.getJwtToken());

    }

    @Getter
    static
    class TokenWrapper {
        private String jwtToken;
    }



}
