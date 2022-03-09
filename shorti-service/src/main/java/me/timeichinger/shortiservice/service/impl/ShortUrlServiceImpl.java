package me.timeichinger.shortiservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.timeichinger.shortiservice.model.User;
import me.timeichinger.shortiservice.repositories.ShortUrlRepository;
import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.repositories.UserRepository;
import me.timeichinger.shortiservice.service.ShortUrlService;
import me.timeichinger.shortiservice.utils.ShortUrlGenerator;
import me.timeichinger.shortiservice.utils.error.ErrorCode;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    @Autowired
    private ShortUrlRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ShortUrl> getAllUrls() {
        List<ShortUrl> urlObjects = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(urlObjects::add);
        return urlObjects;
    }

    @Override
    public List<ShortUrl> getShortUrlsForAuthenticatedUser(@Nullable String userId) throws ShortUrlException {
        if (userId == null) {
            throw new ShortUrlException(ErrorCode.NOT_ALLOWED_TO_VIEW);
        }

        List<ShortUrl> urlObjects = new ArrayList<>();
        repository.findByUser(userId).iterator().forEachRemaining(urlObjects::add);
        return urlObjects;
    }

    @Override
    public ShortUrl createShortUrl(String originUrl, @Nullable String shortUrlStr, @Nullable String userId) throws ShortUrlException {
        log.info("createShortUrl {} {}", originUrl, shortUrlStr);

        if (shortUrlStr != null && userId == null) {
            throw new ShortUrlException(ErrorCode.NOT_ALLOWED_TO_CREATE_CUSTOM_SHORTS);
        }

        ShortUrl shortUrl = ShortUrlGenerator.shortenUrl(originUrl, shortUrlStr, repository);

        if (userId != null) {
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(shortUrl::setCreator);
        }

        log.info("createShortUrl {}", shortUrl);
        return repository.save(shortUrl);
    }

    @Override
    public String getOriginUrl(String shortUrl) {
        ShortUrl url = repository.findByShortUrl(shortUrl);
        return url.getOriginUrl();
    }

    @Override
    public ShortUrl updateShortUrl(String urlId, String newOriginId, @Nullable String userId) throws ShortUrlException {
        Optional<ShortUrl> urlFromDB = repository.findById(urlId);
        if (urlFromDB.isEmpty()) throw new ShortUrlException(ErrorCode.SHORT_URL_NOT_FOUND);

        ShortUrl shortUrl = urlFromDB.get();

        if (userId != null) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent() && !Objects.equals(user.get(),shortUrl.getCreator())) {
                throw new ShortUrlException(ErrorCode.NOT_ALLOWED_TO_EDIT_THIS_SHORT_URL);
            }
        }

        shortUrl.setOriginUrl(newOriginId);

        return repository.save(shortUrl);
    }

    @Override
    public void deleteShortUrl(String urlId, String userId) throws ShortUrlException {

        Optional<ShortUrl> urlFromDB = repository.findById(urlId);
        if (urlFromDB.isEmpty()) throw new ShortUrlException(ErrorCode.SHORT_URL_NOT_FOUND);

        ShortUrl shortUrl = urlFromDB.get();

        if (userId != null) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent() && !Objects.equals(user.get(),shortUrl.getCreator())) {
                throw new ShortUrlException(ErrorCode.NOT_ALLOWED_TO_EDIT_THIS_SHORT_URL);
            }
        }
        repository.deleteById(urlId);
    }
}
