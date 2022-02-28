package me.timeichinger.shortiservice.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@Entity
@Table(name = "short_urls")
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrl {

    @Id
    private String id;
    @Column(name = "short_url")
    private String shortUrl;
    private String originUrl;
    private LocalDateTime createdAt;
    private int numCalls;
    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Override
    public String toString() {
        return "ShortUrl{" +
                "id='" + id + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", originUrl='" + originUrl + '\'' +
                ", createdAt=" + createdAt +
                ", numCalls=" + numCalls +
                ", creator=" + creator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortUrl shortUrl1 = (ShortUrl) o;
        return numCalls == shortUrl1.numCalls &&
                Objects.equals(id, shortUrl1.id) &&
                Objects.equals(shortUrl, shortUrl1.shortUrl) &&
                Objects.equals(originUrl, shortUrl1.originUrl) &&
                Objects.equals(createdAt, shortUrl1.createdAt) &&
                Objects.equals(creator, shortUrl1.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortUrl, originUrl, createdAt, numCalls, creator);
    }
}
