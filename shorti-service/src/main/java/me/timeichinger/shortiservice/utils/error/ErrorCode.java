package me.timeichinger.shortiservice.utils.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    SHORT_URL_NOT_FOUND(400, "The short url was not found"),
    SHORT_URL_ALREADY_EXISTS(400, "The short url is already existing"),
    USER_ALREADY_EXISTS(400, "The user already exists"),
    USER_NOT_FOUND(400, "The user was not found"),
    NOT_ALLOWED_TO_EDIT_THIS_SHORT_URL(400, "You are not allowed to edit this url. Please use a valid user."),
    NOT_ALLOWED_TO_CREATE_CUSTOM_SHORTS(400, "You are not allowed to create custom short urls. Please login for that" ),
    NOT_ALLOWED_TO_VIEW(400, "Not allowed to view this"),
    NOT_ALLOWED_TO_EDIT_USER(400, "Not allowed to edit user");

    private final int statusCode;
    private final String errorDescription;
}
