package me.timeichinger.shortiservice.controller;

import lombok.extern.slf4j.Slf4j;
import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.service.ShortUrlService;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import me.timeichinger.shortiservice.utils.requests.ShortUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/")
public class ShortUrlController {

    @Autowired
    ShortUrlService service;

    @GetMapping("/all")
    public ResponseEntity<?> getAllShortUrlsForUser(HttpServletRequest request) {
        try {
            String userId = String.valueOf(request.getAttribute("user-id"));
            List<ShortUrl> urls = service.getShortUrlsForAuthenticatedUser(userId);
            if (!urls.isEmpty()) {
                urls.iterator().forEachRemaining(shortUrl -> shortUrl.getCreator().setPassword("xxx"));
            }
            return ResponseEntity.ok(urls);
        } catch (ShortUrlException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> getOriginUrl(@PathVariable String shortUrl, HttpServletResponse response) {
        log.info("getOriginUrl");
        try {
            response.sendRedirect(service.getOriginUrl(shortUrl));
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/shortUrl",consumes = "application/json")
    public ResponseEntity createShortUrl(@RequestBody ShortUrlRequest request, @RequestParam(required = false) String shortUrlStr, HttpServletRequest servletRequest) {

        log.info("createShortUrl");
        try {
            String userId = String.valueOf(servletRequest.getAttribute("user-id"));
            ShortUrl urlObj = service.createShortUrl(request.getOriginUrl(), shortUrlStr, Objects.equals(userId, "null") ? null : userId);
            if (urlObj.getCreator() != null) {
                urlObj.getCreator().setPassword("xxx");
            }
            return ResponseEntity.ok(urlObj);
        } catch (ShortUrlException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PatchMapping("/{urlId}")
    public ResponseEntity updateShortUrl(@PathVariable String urlId, @RequestBody ShortUrlRequest request, HttpServletRequest servletRequest) {
        log.info("updateShortUrl");
        try {
            String userId = String.valueOf(servletRequest.getAttribute("user-id"));
            ShortUrl urlObj = service.updateShortUrl(urlId, request.getOriginUrl(), Objects.equals(userId, "null") ? null : userId);
            if (urlObj.getCreator() != null) {
                urlObj.getCreator().setPassword("xxx");
            }
            return ResponseEntity.ok(urlObj);
        } catch (ShortUrlException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{urlId}")
    public ResponseEntity deleteShortUrl(@PathVariable String urlId, HttpServletRequest servletRequest) {
        log.debug("deleteShortUrl");
        String userId = String.valueOf(servletRequest.getAttribute("user-id"));
        try {
            service.deleteShortUrl(urlId, Objects.equals(userId, "null") ? null : userId);
            return ResponseEntity.ok("Successfully deleted");
        } catch (ShortUrlException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

}
