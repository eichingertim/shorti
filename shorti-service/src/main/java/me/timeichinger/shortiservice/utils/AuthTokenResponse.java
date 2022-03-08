package me.timeichinger.shortiservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokenResponse {

    private final String jwtToken;

}
