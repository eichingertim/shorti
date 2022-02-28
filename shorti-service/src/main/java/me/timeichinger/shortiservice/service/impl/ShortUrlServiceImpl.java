package me.timeichinger.shortiservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.timeichinger.shortiservice.database.repositories.ShortUrlRepository;
import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.service.ShortUrlService;
import me.timeichinger.shortiservice.utils.ShortUrlGenerator;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    @Autowired
    ShortUrlRepository repository;

    @Override
    public List<ShortUrl> getAllUrls() {
        List<ShortUrl> urlObjects = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(urlObjects::add);
        return urlObjects;
    }

    @Override
    public ShortUrl createShortUrl(String originUrl, @Nullable String shortUrlStr) throws ShortUrlException {
        log.info("createShortUrl {} {}", originUrl, shortUrlStr);
        ShortUrl shortUrl = ShortUrlGenerator.shortenUrl(originUrl, shortUrlStr, repository);
        log.info("createShortUrl {}", shortUrl);
        return repository.save(shortUrl);
    }

    @Override
    public String getOriginUrl(String shortUrl) {
        ShortUrl url = repository.findByShortUrl(shortUrl);
        return url.getOriginUrl();
    }

    @Override
    public String updateShortUrl(String urlId, String newOriginId) {
        return null;
    }

    @Override
    public String deleteShortUrl(String urlId) {
        return null;
    }
}
