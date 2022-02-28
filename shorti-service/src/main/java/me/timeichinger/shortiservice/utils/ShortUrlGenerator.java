package me.timeichinger.shortiservice.utils;

import me.timeichinger.shortiservice.database.repositories.ShortUrlRepository;
import me.timeichinger.shortiservice.model.ShortUrl;
import me.timeichinger.shortiservice.utils.error.ErrorCode;
import me.timeichinger.shortiservice.utils.error.ShortUrlException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class ShortUrlGenerator {

    public static ShortUrl shortenUrl(String originUrl, @Nullable String shortUrl, ShortUrlRepository repository) throws ShortUrlException {
        if (shortUrl != null) {
            boolean found = repository.findByShortUrl(shortUrl) != null;
            if (found) throw new ShortUrlException(ErrorCode.SHORT_URL_ALREADY_EXISTS);
        }

        return buildShortUrl(originUrl, shortUrl);
    }

    private static ShortUrl buildShortUrl(String originUrl, @Nullable String shortUrl) {
        return ShortUrl.builder()
                .id(UUID.randomUUID().toString())
                .originUrl(originUrl)
                .shortUrl(shortUrl == null ? generate7DigitAlphaNumericString() : shortUrl)
                .createdAt(LocalDateTime.now()).build();

    }

    private static String generate7DigitAlphaNumericString() {
        return RandomStringUtils.randomAlphanumeric(7);
    }

}
