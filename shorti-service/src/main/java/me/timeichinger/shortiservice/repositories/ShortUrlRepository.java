package me.timeichinger.shortiservice.repositories;


import me.timeichinger.shortiservice.model.ShortUrl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlRepository extends CrudRepository<ShortUrl, String> {
    @Query(value = "SELECT * FROM short_urls WHERE short_url = ?1", nativeQuery = true)
    ShortUrl findByShortUrl(String shortUrl);
}
