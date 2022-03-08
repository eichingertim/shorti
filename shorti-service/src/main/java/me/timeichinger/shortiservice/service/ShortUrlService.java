package me.timeichinger.shortiservice.service;

import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShortUrlService {

    List<ShortUrl> getAllUrls();
    ShortUrl createShortUrl(String originUrl, @Nullable String shortUrlStr, @Nullable String userId) throws ShortUrlException;
    String getOriginUrl(String shortUrl);
    ShortUrl updateShortUrl(String urlId, String newOriginId, @Nullable String userId) throws ShortUrlException;
    void deleteShortUrl(String urlId, String userId) throws ShortUrlException;

}
