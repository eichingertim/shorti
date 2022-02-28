package me.timeichinger.shortiservice.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.service.ShortUrlService;
import me.timeichinger.shortiservice.utils.ShortUrlRequest;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/")
public class ShortUrlController {

    @Autowired
    ShortUrlService service;

    @GetMapping("/all")
    public ResponseEntity<List<ShortUrl>> getAllShortUrls() {
        log.debug("getAllShortUrls");
        return ResponseEntity.ok(service.getAllUrls());
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> getOriginUrl(@PathVariable String shortUrl, HttpServletResponse response) {
        log.debug("getOriginUrl");
        try {
            response.sendRedirect(service.getOriginUrl(shortUrl));
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/shortUrl",consumes = "application/json")
    public ResponseEntity createShortUrl(@RequestBody ShortUrlRequest request, @RequestParam(required = false) String shortUrlStr) {
        log.info("createShortUrl");
        try {
            ShortUrl urlObj = service.createShortUrl(request.getOriginUrl(), shortUrlStr);
            return ResponseEntity.ok(urlObj);
        } catch (ShortUrlException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PatchMapping("/{urlId}")
    public ResponseEntity<ShortUrl> updateShortUrl(@PathVariable String urlId, @RequestParam String newOriginUrl) {
        log.debug("updateShortUrl");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{urlId}")
    public ResponseEntity<ShortUrl> deleteShortUrl(@PathVariable String urlId) {
        log.debug("deleteShortUrl");
        return ResponseEntity.ok().build();
    }

}
