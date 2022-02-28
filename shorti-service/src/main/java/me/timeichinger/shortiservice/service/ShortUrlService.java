package me.timeichinger.shortiservice.service;

import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShortUrlService {

    List<ShortUrl> getAllUrls();
    ShortUrl createShortUrl(String originUrl, @Nullable String shortUrlStr) throws ShortUrlException;
    String getOriginUrl(String shortUrl);
    String updateShortUrl(String urlId, String newOriginId);
    String deleteShortUrl(String urlId);

}
