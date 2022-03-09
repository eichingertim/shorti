package me.timeichinger.shortiservice.controller;

import me.timeichinger.shortiservice.model.User;
import me.timeichinger.shortiservice.service.UserService;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity getUser(HttpServletRequest servletRequest) {
        String userId = String.valueOf(servletRequest.getAttribute("user-id"));
        try {
            User user = userService.getUser(Objects.equals(userId, "null") ? null : userId);
            user.setPassword("xxx");
            return ResponseEntity.ok(user);
        } catch (ShortUrlException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity deleteUser(HttpServletRequest servletRequest) {
        String userId = String.valueOf(servletRequest.getAttribute("user-id"));
        try {
            userService.deleteUser(Objects.equals(userId, "null") ? null : userId);
            return ResponseEntity.ok("Successfully deleted user with id: " + userId);
        } catch (ShortUrlException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

}
