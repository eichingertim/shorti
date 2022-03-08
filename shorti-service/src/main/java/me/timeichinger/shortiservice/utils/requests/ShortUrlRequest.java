package me.timeichinger.shortiservice.utils.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrlRequest {
    private String originUrl;
}
