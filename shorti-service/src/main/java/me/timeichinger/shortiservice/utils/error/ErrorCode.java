package me.timeichinger.shortiservice.utils.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    SHORT_URL_NOT_FOUND(400, "The short url was not found"),
    SHORT_URL_ALREADY_EXISTS(400, "The short url is already existing"),
    USER_ALREADY_EXISTS(400, "The user already exists");

    private final int statusCode;
    private final String errorDescription;
}
