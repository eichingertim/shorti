package me.timeichinger.shortiservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrlRequest {
    private String originUrl;
}
