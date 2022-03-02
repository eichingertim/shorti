package me.timeichinger.shortiservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.timeichinger.shortiservice.repositories.ShortUrlRepository;
import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.service.ShortUrlService;
import me.timeichinger.shortiservice.utils.ShortUrlGenerator;
import me.timeichinger.shortiservice.utils.error.ErrorCode;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ShortUrl updateShortUrl(String urlId, String newOriginId) throws ShortUrlException {
        Optional<ShortUrl> urlFromDB = repository.findById(urlId);
        if (urlFromDB.isEmpty()) throw new ShortUrlException(ErrorCode.SHORT_URL_NOT_FOUND);

        ShortUrl shortUrl = urlFromDB.get();
        shortUrl.setOriginUrl(newOriginId);

        return repository.save(shortUrl);
    }

    @Override
    public void deleteShortUrl(String urlId) {
        repository.deleteById(urlId);
    }
}
