package me.timeichinger.shortiservice.repositories;


import me.timeichinger.shortiservice.model.ShortUrl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortUrlRepository extends CrudRepository<ShortUrl, String> {
    @Query(value = "SELECT * FROM short_urls WHERE short_url = ?1", nativeQuery = true)
    ShortUrl findByShortUrl(String shortUrl);

    @Query(value = "SELECT * FROM short_urls WHERE creator_id = ?1", nativeQuery = true)
    List<ShortUrl> findByUser(String userId);
}
