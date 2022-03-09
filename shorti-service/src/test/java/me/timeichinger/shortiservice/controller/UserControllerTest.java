package me.timeichinger.shortiservice.controller;

import com.google.gson.JsonObject;
import me.timeichinger.shortiservice.model.User;
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
public class UserControllerTest {

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
    void getUserDetailsForLoggedInUser() {
        String request = String.format("http://localhost:%d/user",port);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<User> user = restTemplate.exchange(request, HttpMethod.GET,entity, User.class);

        assertThat(user.getBody()).isNotNull();
        assertThat(user.getBody().getUsername()).isNotNull();
        assertThat(user.getBody().getUsername()).isEqualTo("testuser123");
        assertThat(user.getBody().getEmail()).isNotNull();
    }

    @Test
    void deleteLoggedInUser() {
        String request = String.format("http://localhost:%d/user",port);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> res = restTemplate.exchange(request, HttpMethod.DELETE,entity, String.class);

        assertThat(res.getBody()).isNotNull();

        String request1 = String.format("http://localhost:%d/user",port);

        ResponseEntity<User> user = restTemplate.exchange(request, HttpMethod.GET,entity, User.class);
        assertThat(user.getStatusCode().is5xxServerError()).isEqualTo(true);
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
        ShortUrlControllerTest.TokenWrapper wrapper = restTemplate.postForObject(loginRequest, userLoginRequest, ShortUrlControllerTest.TokenWrapper.class);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + wrapper.getJwtToken());

    }

}
